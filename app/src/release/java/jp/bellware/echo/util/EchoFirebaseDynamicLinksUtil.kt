package jp.bellware.echo.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks

object EchoFirebaseDynamicLinksUtil {
    /**
     * Firebase Dynamic LinksのIntentを処理する
     * @param activity
     * @param intent
     * @param onNext 処理完了後に呼ばれる処理
     */
    fun process(activity: Activity, intent: Intent, onNext: (type: String?) -> Unit) {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent).addOnSuccessListener(activity) { pendingDynamicLinkData ->
                    var deepLink: Uri? = null
                    deepLink = pendingDynamicLinkData?.link
                    var type: String? = null
                    deepLink?.let {
                        type = it.getQueryParameter("type")
                    }
                    onNext(type)
                }.addOnFailureListener {
                    onNext(null)
                }

    }
}
