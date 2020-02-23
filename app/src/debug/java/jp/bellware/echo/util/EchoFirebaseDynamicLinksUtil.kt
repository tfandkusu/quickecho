package jp.bellware.echo.util

import android.app.Activity
import android.content.Intent

object EchoFirebaseDynamicLinksUtil {
    fun process(activity: Activity, intent: Intent, onNext: (type: String?) -> Unit) {
        onNext(null)
    }
}