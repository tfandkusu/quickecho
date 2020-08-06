package jp.bellware.echo.view.main

import android.os.Handler
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel

/**
 * タイマー担当
 */
class TimerViewHelper @ViewModelInject constructor() : ViewModel() {
    private val handler = Handler()


    companion object {
        /**
         * 録音可能時間
         */
        private const val MAX_RECORD_TIME = 2 * 60 * 1000L
    }


    private var runnable: Runnable? = null

    /**
     * タイマー開始
     * @param task 時間になったら呼ばれるタスク
     */
    fun start(task: () -> Unit) {
        cancel()
        runnable = Runnable {
            task()
        }
        runnable?.let {
            handler.postDelayed(it, MAX_RECORD_TIME)
        }
    }

    /**
     * タイマーキャンセル
     */
    fun cancel() {
        runnable?.let {
            handler.removeCallbacks(it)
            runnable = null
        }
    }


    override fun onCleared() {
        cancel()
    }
}
