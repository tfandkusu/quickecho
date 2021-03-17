package jp.bellware.echo.util

import android.app.Activity
import android.content.Intent

/**
 * debugビルド用の何もしない実装
 */
object EchoFirebaseDynamicLinksUtil {
    fun process(activity: Activity, intent: Intent, onNext: (type: String?) -> Unit) {
        onNext(null)
    }
}
