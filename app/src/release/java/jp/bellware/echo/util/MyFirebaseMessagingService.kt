package jp.bellware.echo.util

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import jp.bellware.echo.R
import jp.bellware.echo.start.StartActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {

        private const val NOTIFICATION_ID = 1
    }

    override fun onNewToken(token: String) {
        BWU.log("onNewToken $token")
    }

    override fun onMessageReceived(payload: RemoteMessage) {
        val message = payload.data["message"] ?: ""
        val url = payload.data["url"] ?: ""
        if (url.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
            val notification = NotificationCompat.Builder(this, StartActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_small_icon)
                .setContentTitle(getString(R.string.app_name_long))
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
            with(NotificationManagerCompat.from(this)) {
                notify(NOTIFICATION_ID, notification)
            }
        }
    }
}
