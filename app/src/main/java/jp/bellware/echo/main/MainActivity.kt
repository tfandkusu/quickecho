package jp.bellware.echo.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.AudioManager
import android.os.Handler
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View

import jp.bellware.echo.R

import jp.bellware.echo.setting.SettingActivity
import jp.bellware.util.BWU
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_control.*
import kotlinx.android.synthetic.main.main_status.*


class MainActivity : AppCompatActivity(), MainCallback {

    private val handler = Handler()


    /**
     * 警告担当
     */
    private val wh = WarningHandler()

    /**
     * 表示FPS
     */
    private var fps: Int = 0

    /**
     * ボリュームの設定担当
     */
    private lateinit var audioManager: AudioManager


    /**
     * アニメ
     */
    private val animator = QRecAnimator()

    /**
     * DP
     */
    private var dp: Float = 0f

//    /**
//     * 状態画像+視覚的ボリューム
//     */
//    @BindView(R.id.status)
//    internal var statusFrameLayout: FrameLayout? = null
//
//    /**
//     * 状態画像
//     */
//    @BindView(R.id.status_image)
//    internal var statusImageView: ImageView? = null
//
//    /**
//     * 視覚的ボリューム
//     */
//    @BindView(R.id.visual_volume)
//    internal var vvv: VisualVolumeView? = null
//
//    /**
//     * 波紋エフェクト
//     */
//    @BindView(R.id.explosion)
//    internal var explosionView: ExplosionView? = null
//
//    /**
//     * 録音ボタン
//     */
//    @BindView(R.id.record)
//    internal var recordButton: MyFAB? = null
//    /**
//     * 再生ボタン
//     */
//    @BindView(R.id.play)
//    internal var playButton: MyFAB? = null
//    /**
//     * 再再生ボタン
//     */
//    @BindView(R.id.replay)
//    internal var replayButton: MyFAB? = null
//    /**
//     * 停止ボタン
//     */
//    @BindView(R.id.stop)
//    internal var stopButton: MyFAB? = null
//
//    /**
//     * 削除ボタン
//     */
//    @BindView(R.id.delete)
//    internal var deleteButton: MyFAB? = null

    /**
     * FPS計算タスク
     */
    private val fpsTask = object : Runnable {
        override fun run() {
            BWU.log("fps = $fps")
            fps = 0
            handler.postDelayed(this, 1000)
        }
    }


    /**
     * 録音サービス
     */
    private var service: MainService? = null

    private val conn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            service = (binder as MainService.MainServiceBinder).service
            service!!.setCallback(this@MainActivity)
            service!!.onSettingUpdated()
        }

        override fun onServiceDisconnected(name: ComponentName) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BWU.log("MainActivity#onCreate")
        setContentView(R.layout.activity_main)
        //ツールバー設定
        this.setSupportActionBar(toolbar)
        //広告の設定
        setUpAd()
        //ボリューム調整を音楽にする
        volumeControlStream = AudioManager.STREAM_MUSIC
        //AudioManagerを取得
        this.audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        //警告担当
        wh.onCreate(this)
        //dpを取得
        dp = resources.displayMetrics.density
        //クリックイベント(仮)
        record.setOnClickListener {
            service?.let{
                service!!.onRecordClicked()
            }
        }

        play.setOnClickListener {
            service?.let{
                service!!.onPlayClicked()
            }
        }

        replay.setOnClickListener {
            service?.let{
                service!!.onReplayClicked()
            }
        }

        stop.setOnClickListener {
            service?.let{
                service!!.onStopClicked()
            }
        }

        delete.setOnClickListener {
            service?.let{
                service!!.onDeleteClicked()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        BWU.log("MainActivity#onStart")
        //録音サービスを開始
        val intent = Intent(this, MainService::class.java)
        startService(intent)
        bindService(intent, conn, 0)
    }

    override fun onResume() {
        super.onResume()
        BWU.log("MainActivity#onResume")
        wh.onResume()
        if (this.audioManager!!.getStreamVolume(AudioManager.STREAM_MUSIC) == 0) {
            wh.show(R.string.warning_volume)
        }
    }

    override fun onPause() {
        super.onPause()
        BWU.log("MainActivity#onPause")
        handler.removeCallbacks(fpsTask)
        wh.onPause()
    }

    override fun onStop() {
        super.onStop()
        BWU.log("MainActivity#onStop")
        service?.let {
            service!!.setCallback(null)
            service = null
        }
        unbindService(conn)
    }

    override fun onBackPressed() {
        if (service != null) {
            if (service!!.onBackPressed()) {

            } else {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        BWU.log("MainActivity#onDestroy")
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
            if (service != null) {
                service!!.onSettingUpdated()
            }
        }
    }

    /**
     * 設定画面
     */
    private fun callSettingActivity() {
        val intent = Intent(this, SettingActivity::class.java)
        startActivityForResult(intent, CODE_SETTING)
    }

    /**
     * 広告を設定する
     */
    private fun setUpAd() {
        //        AdView mAdView = (AdView) findViewById(R.id.ad);
        //        AdRequest adRequest = new AdRequest.Builder().build();
        //        mAdView.loadAd(adRequest);
    }

    /**
     * 録音ボタンがクリックされた時のイベント
     */
    fun onRecordClicked() {
        if (service != null)
            service!!.onRecordClicked()

    }

    /**
     * 再生ボタンがクリックされた時のイベント
     */
    fun onPlayClicked() {
        if (service != null)
            service!!.onPlayClicked()
    }

    /**
     * 再再生ボタンがクリックされた時のイベント
     */
    fun onReplayClicked() {
        if (service != null) {
            service!!.onReplayClicked()
        }
    }

    /**
     * 停止ボタンがクリックされた時のイベント
     */
    fun onStopClicked() {
        if (service != null) {
            service!!.onStopClicked()
        }
    }

    /**
     * 削除ボタンがクリックされた時のイベント
     */
    fun onDeleteClicked() {
        if (service != null) {
            service!!.onDeleteClicked()
        }
    }


    /**
     * 状態を更新する
     */
    override fun onUpdateStatus(animation: Boolean, status: QRecStatus) {
        BWU.log("MainActivity#onUpdateStatus $status")
        //ステータスビュー
        if (status == QRecStatus.INIT) {
            //初期化中
            //何も表示しない
            statusFrame.visibility = View.INVISIBLE
            record.visibility = View.INVISIBLE
            play.visibility = View.INVISIBLE
            record.visibility = View.INVISIBLE
            stop.visibility = View.INVISIBLE
            delete.visibility = View.INVISIBLE
        } else if (status == QRecStatus.DELETE_RECORDING || status == QRecStatus.DELETE_PLAYING) {
            //アニメーション開始
            if (animation) {
                animator.startDeleteAnimation(statusFrame)
                animator.startDeleteAnimation(delete)
                if (statusFrame == QRecStatus.DELETE_PLAYING) {
                    animator.startDeleteAnimation(replay)
                    animator.startDeleteAnimation(stop)
                } else {
                    replay.visibility = View.INVISIBLE
                    stop.visibility = View.INVISIBLE
                }
            } else {
                statusFrame.visibility = View.INVISIBLE
                record.visibility = View.VISIBLE
                play.visibility = View.INVISIBLE
                replay.visibility = View.INVISIBLE
                stop.visibility = View.INVISIBLE
                delete.visibility = View.INVISIBLE
            }
        } else if (status == QRecStatus.READY_FIRST || status == QRecStatus.READY) {
            //録音ボタンにする
            statusFrame.visibility = View.INVISIBLE
            record.visibility = View.VISIBLE
            play.visibility = View.INVISIBLE
            replay.visibility = View.INVISIBLE
            stop.visibility = View.INVISIBLE
            delete.visibility = View.INVISIBLE
        } else if (status == QRecStatus.STOP) {
            //停止状態
            statusFrame.visibility = View.VISIBLE
            statusImage.setImageResource(R.drawable.speaker_48dp)
            record.visibility = View.VISIBLE
            play.visibility = View.INVISIBLE
            replay.visibility = View.VISIBLE
            stop.visibility = View.VISIBLE
            delete.visibility = View.VISIBLE
        } else if (status == QRecStatus.STARTING_RECORD) {
            //スピーカーアイコン表示
            statusImage.setImageResource(R.drawable.microphone_48dp)
            //再生ボタンにする
            record.visibility = View.INVISIBLE
            play.visibility = View.VISIBLE
            //サブコントロールは非表示
            replay.visibility = View.INVISIBLE
            stop.visibility = View.INVISIBLE
            if (animation) {
                //爆発エフェクト
                explosion.startRecordAnimation()
                //ステータスはフェードイン
                animator.fadeIn(statusFrame)
                //削除ボタンはフェードイン
                if (delete.visibility == View.INVISIBLE) {
                    animator.fadeIn(delete)
                }
            } else {
                statusFrame.visibility = View.VISIBLE
                delete.visibility = View.VISIBLE
            }

        } else if (status == QRecStatus.RECORDING) {
            statusFrame.visibility = View.VISIBLE
            statusImage.setImageResource(R.drawable.microphone_48dp)
            record.visibility = View.INVISIBLE
            play.visibility = View.VISIBLE
            replay.visibility = View.INVISIBLE
            stop.visibility = View.INVISIBLE
            delete.visibility = View.VISIBLE
        } else if (status == QRecStatus.STOPPING_RECORD) {
            //スピーカーを表示
            statusImage.setImageResource(R.drawable.speaker_48dp)
            //録音ボタンにする
            record.visibility = View.VISIBLE
            play.visibility = View.INVISIBLE
            if (animation) {
                //ステータスを表示
                animator.fadeIn(statusFrame)
                //サブコントロール表示
                animator.fadeIn(replay)
                animator.fadeIn(stop)
            } else {
                statusFrame.visibility = View.VISIBLE
                replay.visibility = View.VISIBLE
                stop.visibility = View.VISIBLE
            }
        } else if (status == QRecStatus.PLAYING) {
            statusFrame.visibility = View.VISIBLE
            statusImage.setImageResource(R.drawable.speaker_48dp)
            record.visibility = View.VISIBLE
            play.visibility = View.INVISIBLE
            replay.visibility = View.VISIBLE
            stop.visibility = View.VISIBLE
            delete.visibility = View.VISIBLE
        } else if (statusFrame == QRecStatus.STOPPING_PLAYING) {
        }
    }

    override fun onUpdateVolume(volume: Float) {
        visualVolume.setVolume(volume)
    }

    override fun onShowWarningMessage(resId: Int) {
        wh.show(resId)
    }

    companion object {


        private val CODE_SETTING = 1
    }
}
