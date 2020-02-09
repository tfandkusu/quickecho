package jp.bellware.echo.util

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("Takada", "onNewToken $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("Takada", "onMessageReceived %s".format(message.data))
    }
}