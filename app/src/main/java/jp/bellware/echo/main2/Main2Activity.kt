package jp.bellware.echo.main2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import jp.bellware.echo.R
import jp.bellware.echo.actioncreator.MainActionCreator
import jp.bellware.echo.setting.SettingActivity
import jp.bellware.echo.store.MainStore
import jp.bellware.echo.store.QrecSoundEffect
import jp.bellware.echo.store.RPRequest
import jp.bellware.echo.store.VisualVolumeRequest
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.main_control2.*
import kotlinx.android.synthetic.main.main_status2.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class Main2Activity : AppCompatActivity() {

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
     * TODO ViewHelperにする
     */
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setSupportActionBar(toolbar)
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
        soundEffect.onCreate(this) {
            actionCreator.onSoundLoaded()
        }
        // 録音ボタンが押せる
        store.recordClickable.observe(this, Observer {
            if (it == true) {
                // 録音ボタンが押された
                record.setOnClickListener {
                    actionCreator.onRecordClick()
                }
            }
        })

        // StoreとViewを繋げる
        store.status.observe(this, Observer { flag ->
            flag?.let {
                if (it)
                    statusFrame.visibility = View.VISIBLE
                else
                    statusFrame.visibility = View.INVISIBLE
            }
        })
        store.icon.observe(this, Observer { resId ->
            resId?.let {
                statusImage.setImageResource(it)
            }
        })
        store.explosion.observe(this, Observer {
            if (it == true)
                explosionView.startRecordAnimation()
        })
        store.record.observe(this, Observer { flag ->
            flag?.let {
                if (it)
                    record.show()
                else
                    record.hide()
            }
        })
        store.play.observe(this, Observer { flag ->
            flag?.let {
                if (it)
                    play.show()
                else
                    play.hide()
            }
        })
        store.stop.observe(this, Observer { flag ->
            flag?.let {
                if (it)
                    stop.show()
                else
                    stop.hide()
            }
        })
        store.replay.observe(this, Observer { flag ->
            flag?.let {
                if (it)
                    replay.show()
                else
                    replay.hide()
            }
        })
        store.delete.observe(this, Observer { flag ->
            flag?.let {
                if (it)
                    delete.show()
                else
                    delete.hide()
            }
        })
        // StoreをViewHelperをつなげる
        store.soundEffect.observe(this, Observer {
            when (it) {
                QrecSoundEffect.START -> {
                    soundEffect.start()
                    // 効果音が終わったら録音開始
                    handler.postDelayed({
                        startRecord()
                    }, 500)
                }
                QrecSoundEffect.PLAY ->
                    soundEffect.play()
                QrecSoundEffect.DELETE ->
                    soundEffect.delete()
                null -> {
                }
            }
        })
        store.requestForPlay.observe(this, Observer {
            when (it) {
                RPRequest.START ->
                    playViewHelper.play {
                        // TODO 終了時の処理
                    }
                RPRequest.STOP ->
                    playViewHelper.stop()
                null -> {
                }
            }
        })
        store.requestForRecord.observe(this, Observer {
            when (it) {
                RPRequest.START ->
                    recordViewHelper.start()
                RPRequest.STOP ->
                    recordViewHelper.stop()
                null -> {

                }
            }
        })
        store.visualVolume.observe(this, Observer {
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
    }

    override fun onResume() {
        super.onResume()
        visualVolumeViewHelper.onResume()
    }

    override fun onPause() {
        super.onPause()
        visualVolumeViewHelper.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
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
        }
    }

    /**
     * 録音開始(本番)
     */
    private fun startRecord() {
        actionCreator.startRecord()
        // TODO タイムアウトタスク予約
    }

    private fun callSettingActivity() {
        val intent = Intent(this, SettingActivity::class.java)
        startActivityForResult(intent, CODE_SETTING)
    }
}
