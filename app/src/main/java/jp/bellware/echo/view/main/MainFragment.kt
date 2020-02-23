package jp.bellware.echo.view.main


import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import jp.bellware.echo.R
import jp.bellware.echo.actioncreator.MainActionCreator
import jp.bellware.echo.store.*
import jp.bellware.echo.view.setting.SettingActivity
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.main_control.*
import kotlinx.android.synthetic.main.main_status.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : Fragment() {


    companion object {
        private const val CODE_SETTING = 1
    }

    /**
     * 表示制御担当
     */
    private val store: MainStore by viewModel()

    /**
     * ユーザ操作、音声系ViewHelperからのコールバックを受けて、アクションを発行する担当
     */
    private val actionCreator: MainActionCreator by inject()

    /**
     * 効果音担当ViewHelper
     */
    private val soundEffect: SoundEffectViewHelper by viewModel()

    /**
     * 録音担当ViewHelper
     */
    private val recordViewHelper: RecordViewHelper by viewModel()

    /**
     * 再生担当ViewHelper
     */
    private val playViewHelper: PlayViewHelper by viewModel()

    /**
     * 視覚的ボリューム担当ViewHelper
     */
    private val visualVolumeViewHelper: VisualVolumeViewHelper by viewModel()

    /**
     * Viewのアニメーション担当
     */
    private val animatorViewHelper: AnimatorViewHelper by inject()

    /**
     * 録音時間計測担当
     */
    private val timerViewHelper: TimerViewHelper by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() { // Handle the back button event
                actionCreator.onBackPressed()
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
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        // 効果音読み込み
        soundEffect.onCreate {
            actionCreator.onSoundLoaded()
        }
        // StoreとViewを繋げる
        store.status.observe(viewLifecycleOwner, Observer {
            animatorViewHelper.apply(statusFrame, it)
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
            animatorViewHelper.apply(record, it)
        })
        store.play.observe(viewLifecycleOwner, Observer {
            animatorViewHelper.apply(play, it)
        })
        store.stop.observe(viewLifecycleOwner, Observer {
            animatorViewHelper.apply(stop, it)
        })
        store.replay.observe(viewLifecycleOwner, Observer {
            animatorViewHelper.apply(replay, it)
        })
        store.delete.observe(viewLifecycleOwner, Observer {
            animatorViewHelper.apply(delete, it)
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
                    recordViewHelper.stop()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CODE_SETTING) {
            soundEffect.onSettingUpdate()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun callSettingActivity() {
        val intent = Intent(requireContext(), SettingActivity::class.java)
        startActivityForResult(intent, CODE_SETTING)
    }

    /**
     * 警告を表示する
     * @param resId 文字列リソース
     */
    private fun showWarning(resId: Int) {
        warning_card.visibility = View.VISIBLE
        warning_text.setText(resId)
        val animator = ObjectAnimator.ofFloat(warning_card, View.ALPHA, 1f, 0f)
        animator.startDelay = 3000
        animator.duration = 300
        animator.start()
    }
}
