package com.inaki.firstkotlinapp

import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseService : FirebaseMessagingService() {

    // constructing our firebase service
    // extends the firebase messaging service

    // here we initialize the notification manager
    // the manager will handle the way you notify your notification
    private val notManager: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(baseContext)
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        // this method will update the token to talk to firebase
        // here we are displaying token to logcat
        Log.d(TAG, "new token.... $p0")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // try/catch is used to handle exceptions
        try {
            // here we check for null notification
            remoteMessage.notification?.let {
                // here we are displaying the notification with the message received
                // passing the title and body of the notification
                showNotification(it.title, it.body)
            } ?:
            // if notification is null we show the message from data
            showNotification(remoteMessage.data["title"], remoteMessage.data["message"])

        } catch (e : Exception) {
            // we catch any exception and log to logcat
            Log.e(TAG, e.stackTraceToString())
        }
    }

    // this method will have all the logic to show notification
    // the title and text should come form the message received
    // attributes as nullables
    private fun showNotification(title: String?, body: String?) {
        // here we are creating out notification to be displayed to the user
        NotificationCompat.Builder(baseContext, MainActivity2.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
            .apply {
                // here we notify for the new notification thru the manager
                // apply block will allow us to handle this action with 'this' as the notification value
                notManager.notify(NOT_ID, this)
            }
    }

    companion object {
        // setting a tag for logcat
        const val TAG = "FirebaseService"
        // variable for notification ID
        const val NOT_ID = 432
    }
}