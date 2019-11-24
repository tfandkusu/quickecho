package jp.bellware.echo.main2

import android.os.Handler
import androidx.lifecycle.ViewModel


/**
 * 視覚的ボリューム隔離クラス
 */
class VisualVolumeViewHelper : ViewModel() {

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
    var callBack = object : Callback {
        override fun getRecordVisualVolume(): Float {
            return 0f
        }

        override fun getPlayVisualVolume(): Float {
            return 0f
        }

        override fun onUpdateVolume(volume: Float) {
        }
    }


    /**
     * 視覚的ボリューム更新タスク
     */
    private val updateTask = object : Runnable {
        override fun run() {
            if (recording) {
                callBack.onUpdateVolume(callBack.getRecordVisualVolume())
            } else if (playing) {
                callBack.onUpdateVolume(callBack.getPlayVisualVolume())
            } else {
                callBack.onUpdateVolume(0f)
            }

            handler.postDelayed(this, (1000 / 30).toLong())
        }
    }

    interface Callback {
        /**
         * 録音の視覚的ボリュームを取得する
         * @return 録音の視覚的ボリューム
         */
        fun getRecordVisualVolume(): Float

        /**
         * 再生の視覚的ボリュームを取得する
         * @return 再生の視覚的ボリューム
         */
        fun getPlayVisualVolume(): Float

        /**
         * 視覚的ボリュームを更新する
         * @param volume 更新先ボリューム
         */
        fun onUpdateVolume(volume: Float)
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
        callBack.onUpdateVolume(0f)
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
