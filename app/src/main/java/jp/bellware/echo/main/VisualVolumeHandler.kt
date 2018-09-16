package jp.bellware.echo.main

import android.content.Context
import android.os.Handler


/**
 * 視覚的ボリューム隔離クラス
 */
class VisualVolumeHandler {

    /**
     * アニメーション担当
     */
    private val animator = QRecAnimator()

    private val handler = Handler()

    /**
     * 録音中フラグ
     */
    private var recording = false

    /**
     * 再生中フラグ
     */
    private var playing = false


    /**
     * コールバック
     */
    private lateinit var cb: Callback


    /**
     * 視覚的ボリューム更新タスク
     */
    private val updateTask = object : Runnable {
        override fun run() {
            //TODO null安全にする
            if (recording) {
                cb.onUpdateVolume(cb.getRecordVisualVolume())
            } else if (playing) {
                cb.onUpdateVolume(cb.getPlayVisualVolume())
            } else {
                cb.onUpdateVolume(0f)
            }

            handler.postDelayed(this, (1000 / 30).toLong())
        }
    }

    interface Callback {
        fun getRecordVisualVolume() : Float

        fun getPlayVisualVolume(): Float

        fun onUpdateVolume(volume: Float)
    }


    fun onCreate(context: Context, cb: Callback) {
        this.cb = cb
    }

    fun onResume() {

    }

    fun onPause() {
        handler.removeCallbacks(updateTask)
        playing = false
        recording = false
    }

    fun reset() {
        playing = false
        recording = false
        cb.onUpdateVolume(0f)
    }


    fun play() {
        playing = true
        recording = false
        handler.removeCallbacks(updateTask)
        updateTask.run()
    }

    fun record() {
        playing = false
        recording = true
        handler.removeCallbacks(updateTask)
        updateTask.run()
    }

    fun stop() {
        playing = false
        recording = false
        handler.removeCallbacks(updateTask)
    }


}
