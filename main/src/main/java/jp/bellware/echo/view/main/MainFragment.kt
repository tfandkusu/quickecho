package jp.bellware.echo.view.main


import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.media.AudioManager
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import dagger.hilt.android.AndroidEntryPoint
import jp.bellware.echo.actioncreator.MainActionCreator
import jp.bellware.echo.main.R
import jp.bellware.echo.store.*
import jp.bellware.echo.view.memo.SoundMemoActivityAlias
import jp.bellware.echo.view.setting.SettingActivityAlias
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.main_control.*
import kotlinx.android.synthetic.main.main_progress.*
import kotlinx.android.synthetic.main.main_sound_memo_button.*
import kotlinx.android.synthetic.main.main_status.*
import kotlinx.coroutines.InternalCoroutinesApi


@AndroidEntryPoint
class MainFragment : Fragment() {

    /**
     * 表示制御担当
     */
    private val store: MainStore by viewModels()

    /**
     * ユーザ操作、音声系ViewHelperからのコールバックを受けて、アクションを発行する担当
     */
    private val actionCreator: MainActionCreator by viewModels()

    /**
     * 効果音担当ViewHelper
     */
    private val soundEffect: SoundEffectViewHelper by viewModels()

    /**
     * 録音担当ViewHelper
     */
    private val recordViewHelper: RecordViewHelper by viewModels()

    /**
     * 再生担当ViewHelper
     */
    private val playViewHelper: PlayViewHelper by viewModels()

    /**
     * 視覚的ボリューム担当ViewHelper
     */
    private val visualVolumeViewHelper: VisualVolumeViewHelper by viewModels()

    /**
     * 録音時間計測担当
     */
    private val timerViewHelper: TimerViewHelper by viewModels()


    companion object {
        /**
         * 再生中または停止中
         */
        private const val EXTRA_PLAY_OR_STOP = "playOrStop"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() { // Handle the back button event
                if (store.progress.value != true) {
                    actionCreator.onBackPressed()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
        store.overrideBackKey.observe(this, Observer { flag ->
            flag?.let {
                callback.isEnabled = it
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 音声メモボタンDrawableの作成
        setUpSoundMemoButtonBackground()

        // ボリューム情報の接続
        visualVolumeViewHelper.callBack = object : VisualVolumeViewHelper.Callback {
            override fun getRecordVisualVolume(): Float {
                return recordViewHelper.visualVolume
            }

            override fun getPlayVisualVolume(): Float {
                return playViewHelper.visualVolume
            }

            override fun onUpdateVolume(volume: Float) {
                visualVolume.setVolume(volume)
            }

        }
        // 再生または停止状態でプロセスキルフラグの取得
        val playOrStop = savedInstanceState?.getBoolean(EXTRA_PLAY_OR_STOP, false) ?: false
        // 効果音読み込み
        soundEffect.onCreate(requireContext()) {
            actionCreator.onSoundLoaded(playOrStop)
        }
        // StoreとViewを繋げる
        store.status.observe(viewLifecycleOwner, Observer {
            AnimatorViewHelper.apply(statusFrame, it)
        })
        store.icon.observe(viewLifecycleOwner, Observer {
            when (it) {
                StatusIcon.RECORD -> {
                    statusImage.setImageResource(R.drawable.microphone_48dp)
                }
                StatusIcon.PLAY -> {
                    statusImage.setImageResource(R.drawable.speaker_48dp)
                }
                else -> {
                }
            }
        })
        store.explosion.observe(viewLifecycleOwner, Observer {
            if (it == true)
                explosionView.startRecordAnimation()
        })
        store.record.observe(viewLifecycleOwner, Observer {
            AnimatorViewHelper.apply(record, it)
        })
        store.play.observe(viewLifecycleOwner, Observer {
            AnimatorViewHelper.apply(play, it)
        })
        store.stop.observe(viewLifecycleOwner, Observer {
            AnimatorViewHelper.apply(stop, it)
        })
        store.replay.observe(viewLifecycleOwner, Observer {
            AnimatorViewHelper.apply(replay, it)
        })
        store.delete.observe(viewLifecycleOwner, Observer {
            AnimatorViewHelper.apply(delete, it)
        })
        // StoreをViewHelperをつなげる
        store.soundEffect.observe(viewLifecycleOwner, Observer {
            when (it) {
                QrecSoundEffect.START -> {
                    soundEffect.start()
                }
                QrecSoundEffect.PLAY ->
                    soundEffect.play()
                QrecSoundEffect.DELETE ->
                    soundEffect.delete()
                null -> {
                }
            }
        })
        store.requestForPlay.observe(viewLifecycleOwner, Observer {
            when (it) {
                RPRequest.START ->
                    playViewHelper.play {
                    }
                RPRequest.STOP ->
                    playViewHelper.stop()
                null -> {
                }
            }
        })
        store.requestForRecord.observe(viewLifecycleOwner, Observer {
            when (it) {
                RPRequest.START ->
                    recordViewHelper.start()
                RPRequest.STOP ->
                    recordViewHelper.stop(false)
                RPRequest.STOP_AND_SAVE ->
                    recordViewHelper.stop(true)
                null -> {

                }
            }
        })
        store.visualVolume.observe(viewLifecycleOwner, Observer {
            when (it) {
                VisualVolumeRequest.RESET ->
                    visualVolumeViewHelper.reset()
                VisualVolumeRequest.RECORD ->
                    visualVolumeViewHelper.record()
                VisualVolumeRequest.PLAY ->
                    visualVolumeViewHelper.play()
                VisualVolumeRequest.STOP ->
                    visualVolumeViewHelper.stop()
                null -> {
                }
            }
        })
        store.warning.observe(viewLifecycleOwner, Observer {
            when (it) {
                WarningMessage.MUTE ->
                    showWarning(R.string.warning_volume)
                WarningMessage.RECORD_TIME ->
                    showWarning(R.string.warning_time_limit)
                WarningMessage.NO_RECORD ->
                    showWarning(R.string.warning_no_sound)
                null -> {
                }
            }
        })
        store.requestForTimer.observe(viewLifecycleOwner, Observer {
            when (it) {
                TimerRequest.START -> {
                    timerViewHelper.start {
                        actionCreator.onMaxRecordTimeOver(recordViewHelper.isIncludeSound)
                    }
                }
                TimerRequest.CANCEL -> {
                    timerViewHelper.cancel()
                }
                null -> {
                }
            }
        })
        store.clickPlay.observe(viewLifecycleOwner, Observer {
            if (it == true)
                play.performClick()
        })
        store.clickDelete.observe(viewLifecycleOwner, Observer {
            if (it == true)
                delete.performClick()
        })
        store.progress.observe(viewLifecycleOwner) {
            progress.isVisible = it
        }
        // 音声メモ画面を呼び出す
        store.soundMemo.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val intent = Intent(requireContext(), SoundMemoActivityAlias::class.java)
                startActivity(intent)
            }
        })
        // クリックイベント
        // 録音ボタンが押された
        record.setOnClickListener {
            if (store.clickable)
                actionCreator.onRecordClick()
        }
        // 削除ボタンが押された
        delete.setOnClickListener {
            if (store.clickable)
                actionCreator.onDeleteClick()
        }
        // 再生ボタンが押された
        play.setOnClickListener {
            if (store.clickable) {
                actionCreator.onPlayClick(recordViewHelper.isIncludeSound)
            }
        }
        // 再再生ボタンが押された
        replay.setOnClickListener {
            if (store.clickable)
                actionCreator.onReplayClick()
        }
        // 停止ボタンが押された
        stop.setOnClickListener {
            if (store.clickable)
                actionCreator.onStopClick()
        }
        // 音声メモボタンが押された
        soundMemoButton.setOnClickListener {
            actionCreator.onSoundMemoClick()
        }
        // 音声メモボタンは消す
        soundMemoButton.isVisible = false
        progress.setOnClickListener {

        }
    }


    override fun onResume() {
        super.onResume()
        visualVolumeViewHelper.onResume()
        val audioManager = requireActivity().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0) {
            actionCreator.onMute()
        }
    }

    override fun onPause() {
        super.onPause()
        visualVolumeViewHelper.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.setting) {
            callSettingActivity()
            return true
        }
        return false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(EXTRA_PLAY_OR_STOP, store.playOrStop)
    }

    /**
     *  音声メモボタンの背景を設定する
     */
    private fun setUpSoundMemoButtonBackground() {
        val dp = resources.displayMetrics.density
        val model = ShapeAppearanceModel.Builder()
                .setAllCornerSizes(28 * dp)
                .build()
        val drawable = MaterialShapeDrawable(model).apply {
            fillColor =
                    ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.memo))
        }
        soundMemoButton.background = drawable
    }

    /**
     * 警告を表示する
     * @param resId 文字列リソース
     */
    private fun showWarning(resId: Int) {
        warning_card.visibility = View.VISIBLE
        warning_card.alpha = 1f
        warning_text.setText(resId)
        val animator = ObjectAnimator.ofFloat(warning_card, View.ALPHA, 1f, 0f)
        animator.startDelay = 3000
        animator.duration = 300
        animator.start()
    }

    /**
     * 設定画面を呼び出す
     */
    private fun callSettingActivity() {
        val intent = Intent(requireContext(), SettingActivityAlias::class.java)
        startActivity(intent)
    }
}
