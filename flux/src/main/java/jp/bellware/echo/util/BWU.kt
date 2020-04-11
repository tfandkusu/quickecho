package jp.bellware.echo.util

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
     * 例外をログとして出力する
     * @param e 例外
     */
    fun log(e: Exception) {
        Log.d("BWA", "Error", e)
    }
}
