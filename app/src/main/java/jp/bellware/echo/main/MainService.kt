package jp.bellware.echo.main

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import jp.bellware.echo.R
import jp.bellware.echo.analytics.AnalyticsHandler
import jp.bellware.echo.data.QRecStorage
import jp.bellware.echo.setting.SettingFragment
import jp.bellware.util.BWU

/**
 * 録音サービス。
 * 画面回転や画面の大きさが変わっても録音を継続できるようにサービス化している。
 */
class MainService : Service() {

    private val handler = Handler()

    private val endTask = Runnable {
        BWU.log("MainService#endTask")
        stopSelf()
    }

    private val binder = MainServiceBinder()

    /**
     * 効果音担当
     */
    private val seh = SoundEffectHandler()

    /**
     * 視覚的ボリューム担当
     */
    private val vvh = VisualVolumeHandler()

    /**
     * 保存担当
     */
    private val storage = QRecStorage()

    /**
     * 録音担当
     */
    private val record = RecordHandler(storage)

    /**
     * 再生担当
     */
    private val play = PlayHandler(storage)

    /**
     * 分析担当
     */
    private val ah = AnalyticsHandler()


    /**
     * 録音可能時間
     */
    private val timeLimitTask = Runnable {
        if (record.isIncludeSound) {
            status = QRecStatus.STOPPING_RECORD
        } else {
            seh.delete()
            status = QRecStatus.DELETE_RECORDING
        }
        cb?.onShowWarningMessage(R.string.warning_time_limit)
        update()
    }

    /**
     * 遅延タスク
     */
    private var delayTask: Runnable = Runnable { }

    /**
     * 現在の状態
     */
    private var status = QRecStatus.INIT


    /**
     * コールバック
     */
    private var cb: MainCallback? = null

    inner class MainServiceBinder : Binder() {
        val service: MainService
            get() = this@MainService
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }


    override fun onCreate() {
        super.onCreate()
        BWU.log("MainService#onCreate")
        //終了タスクを予約
        handler.postDelayed(endTask, END_TIME.toLong())
        //設定を反映
        onSettingUpdated()
        //視覚的ボリューム
        vvh.onCreate(this, object : VisualVolumeHandler.Callback {
            override fun getRecordVisualVolume(): Float {
                return record.visualVolume
            }

            override fun getPlayVisualVolume(): Float {
                return play.visualVolume
            }

            override fun onUpdateVolume(volume: Float) {
                cb?.onUpdateVolume(volume)
            }
        })
        record.onResume()
        play.onResume()
        vvh.onResume()
        //分析
        ah.onCreate(this)
        update()
    }

    /**
     * コールバックを設定する
     *
     * @param cb
     */
    fun setCallback(cb: MainCallback?) {
        handler.removeCallbacks(endTask)
        if (cb == null) {
            handler.postDelayed(endTask, END_TIME.toLong())
        } else {
            cb.onUpdateStatus(false, status)
            cb.onUpdateVolume(0f)
        }
        this.cb = cb

    }


    override fun onDestroy() {
        super.onDestroy()
        BWU.log("MainService#onDestroy")
        handler.removeCallbacks(timeLimitTask)
        record.onPause()
        play.onPause()
        status = QRecStatus.READY_FIRST
        vvh.onPause()
        seh.onDestroy()
        handler.removeCallbacks(delayTask)
    }

    /**
     * 戻るボタンが押された時に呼ばれる
     *
     * @return
     */
    fun onBackPressed(): Boolean {
        if (status == QRecStatus.RECORDING || status == QRecStatus.STOPPING_RECORD) {
            seh.delete()
            status = QRecStatus.DELETE_RECORDING
            update()
            return true
        } else if (status == QRecStatus.STOP ||
                status == QRecStatus.PLAYING ||
                status == QRecStatus.STOPPING_PLAYING) {
            seh.delete()
            status = QRecStatus.DELETE_PLAYING
            update()
            return true
        } else {
            return false
        }
    }


    fun onSettingUpdated() {
        //設定が更新された時に呼ばれる
        seh.isEnabled = SettingFragment.isSoundEffect(this)
    }

    /**
     * 録音ボタンがクリックされた時のイベント
     */
    fun onRecordClicked() {
        if (status == QRecStatus.READY_FIRST || status == QRecStatus.READY) {
            status = QRecStatus.STARTING_RECORD
            update()
        } else if (status == QRecStatus.STOP) {
            status = QRecStatus.STARTING_RECORD
            update()
        } else if (status == QRecStatus.PLAYING) {
            status = QRecStatus.STARTING_RECORD
            update()
        }
    }

    /**
     * 再生ボタンがクリックされた時のイベント
     */
    fun onPlayClicked() {
        if (status == QRecStatus.RECORDING) {
            if (record.isIncludeSound) {
                status = QRecStatus.STOPPING_RECORD
            } else {
                seh.delete()
                status = QRecStatus.DELETE_RECORDING
                cb?.onShowWarningMessage(R.string.warning_no_sound)
            }
            update()
        }
    }

    /**
     * 再再生ボタンがクリックされた時のイベント
     */
    fun onReplayClicked() {
        if (status == QRecStatus.PLAYING || status == QRecStatus.STOP) {
            status = QRecStatus.PLAYING
            update()
        }
    }

    /**
     * 停止ボタンがクリックされた時のイベント
     */
    fun onStopClicked() {
        if (status == QRecStatus.PLAYING) {
            status = QRecStatus.STOPPING_PLAYING
            update()
            ah.sendAction("stop", (storage.length / 44100).toLong())
        }
    }

    /**
     * 削除ボタンがクリックされた時のイベント
     */
    fun onDeleteClicked() {
        if (status == QRecStatus.RECORDING) {
            seh.delete()
            status = QRecStatus.DELETE_RECORDING
            update()
        } else if (status == QRecStatus.PLAYING || status == QRecStatus.STOP) {
            seh.delete()
            status = QRecStatus.DELETE_PLAYING
            update()
        }
    }

    /**
     * 状態に応じて更新する
     */
    private fun update() {
        if (status == QRecStatus.INIT) {
            //効果音読み込み
            seh.onCreate(this) {
                //初期化完了
                status = QRecStatus.READY_FIRST
                update()
            }
        } else if (status == QRecStatus.DELETE_RECORDING || status == QRecStatus.DELETE_PLAYING) {
            //録音済生取り消し
            play.stop()
            record.stop()
            //タイムリミットタスク解除
            handler.removeCallbacks(timeLimitTask)
            //Analytics
            ah.sendAction("delete", (storage.length / 44100).toLong())
            //削除→録音可能
            this.delayTask = Runnable {
                status = QRecStatus.READY
                update()
            }
            handler.postDelayed(delayTask, 200)
        } else if (status == QRecStatus.READY_FIRST || status == QRecStatus.READY) {
            play.stop()
            record.stop()
        } else if (status == QRecStatus.STOP) {
            //視覚的ボリューム
            vvh.stop()
        } else if (status == QRecStatus.STARTING_RECORD) {
            //音を鳴らす
            seh.start()
            //再生終了
            play.stop()
            //視覚的ボリュームをリセット
            vvh.reset()
            //効果音が鳴り終わったら録音開始
            delayTask = Runnable {
                status = QRecStatus.RECORDING
                update()
            }
            handler.postDelayed(delayTask, 500)
            //イベントを送る
            ah.sendAction("record")
        } else if (status == QRecStatus.RECORDING) {
            //録音開始
            record.start()
            //タイムリミットタスク予約
            handler.postDelayed(timeLimitTask, TIME_LIMIT.toLong())
            //視覚的ボリューム
            vvh.record()
        } else if (status == QRecStatus.STOPPING_RECORD) {
            //録音終了
            record.stop()
            //タイムリミットタスク解除
            handler.removeCallbacks(timeLimitTask)
            //終了SE
            seh.play()
            //視覚的ボリュームをリセット
            vvh.reset()
            //効果音が鳴り終わったら再生開始
            delayTask = Runnable {
                status = QRecStatus.PLAYING
                update()
            }
            handler.postDelayed(delayTask, 550)
        } else if (status == QRecStatus.PLAYING) {
            //最後まで再生したイベント設定
            play.play {
                status = QRecStatus.STOPPING_PLAYING
                update()
            }
            //視覚的ボリュームをリセット
            vvh.reset()
            //視覚的ボリューム
            vvh.play()
            //Analytics
            ah.sendAction("play", (storage.length / 44100).toLong())

        } else if (status == QRecStatus.STOPPING_PLAYING) {
            vvh.reset()
            play.stop()
            status = QRecStatus.STOP
            update()
        }
        //状態更新をActivityに通知
        cb?.onUpdateStatus(true, status)

    }

    companion object {

        /**
         * サービス生存時間は接続Activity消失から3秒後
         */
        private val END_TIME = 2000

        /**
         * 録音時間制限は2分
         */
        private val TIME_LIMIT = 2 * 60 * 1000
    }
}
