package jp.bellware.util

import android.os.Build
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log

/**
 * メソッド１つで済むUtil
 */
object BWU {
    /**
     * メッセージをログとして出力する
     */
    fun log(message: String) {
        Log.d("BWA", message)
    }

    /**
     * Android 5.0以上か判定する
     */
    fun v21(task: Runnable) {
        if (Build.VERSION.SDK_INT >= 21) {
            task.run()
        }
    }

    /**
     * 例外をログとして出力する
     * @param e 例外
     */
    fun log(e: Exception) {
        Log.d("BWA", "Error", e)
    }
}
