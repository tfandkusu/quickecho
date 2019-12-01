package jp.bellware.echo.main2

import android.os.Handler
import androidx.lifecycle.ViewModel

/**
 * 遅延タスク実行担当ViewHelper
 */
class DelayTaskViewHelper : ViewModel() {

    private val handler = Handler()

    /**
     * 予約タスク一覧
     */
    val tasks = mutableListOf<Runnable>()

    fun start(time: Int, task: () -> Unit) {
        val runnable = object : Runnable {
            override fun run() {
                task()
                tasks.remove(this)
            }
        }
        handler.postDelayed(runnable, time.toLong())
        tasks.add(runnable)
    }


    override fun onCleared() {
        tasks.map { handler.removeCallbacks(it) }
    }
}