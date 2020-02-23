package jp.bellware.echo.util

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        BWU.log("onNewToken $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        BWU.log("onMessageReceived %s".format(message.data))
    }
}