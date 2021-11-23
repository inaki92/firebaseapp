package com.inaki.firstkotlinapp

import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val notManager: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(baseContext)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("$tag token --> $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {

            if (remoteMessage.notification != null) {
                Log.d(tag, "notification -> ${remoteMessage.notification?.title}")
                Log.d(tag, "notification -> ${remoteMessage.notification?.body}")
                showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
            } else {
                Log.d(tag, "notification -> ${remoteMessage.data["title"]}")
                Log.d(tag, "notification -> ${remoteMessage.data["message"]}")
                showNotification(remoteMessage.data["title"], remoteMessage.data["message"])
            }

        } catch (e: Exception) {
            Log.e(tag, "error -> ${e.localizedMessage}")
        }
    }

    private fun showNotification(title: String?, body: String?) {
        val notification = Notification.createNotification(baseContext, title, body)
        notManager.notify(321, notification.build())
    }

    companion object {
        private const val tag = "FirebaseMessagingService"
    }

}