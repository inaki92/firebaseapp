package com.inaki.firstkotlinapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.inaki.firstkotlinapp.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding

    private val broadcastManager: LocalBroadcastManager by lazy {
        LocalBroadcastManager.getInstance(baseContext)
    }

    private val myBroadcast = MyBroadcast {
        Log.d("MainActivity2", it)
//        val notificationBuilder = Notification.createNotification(baseContext, it, "body").build()
//        notManager.notify(MY_ID, notificationBuilder)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        broadcastManager.registerReceiver(myBroadcast, MyBroadcast.intentFilter)
    }

    override fun onResume() {
        super.onResume()

        Notification.createNotificationChannel(baseContext)

        intent?.let { mIntent ->
            mIntent.getStringExtra(DATA)?.let {
                binding.textView.text = it
            }
        }

        val mIntent = Intent().apply {
            action = MyBroadcast.MY_ACTION
            putExtra(MyBroadcast.MY_EXTRA, binding.textView.text.toString())
        }

        binding.button2.setOnClickListener {
            broadcastManager.sendBroadcast(mIntent)
        }
    }

    override fun onStop() {
        super.onStop()
        broadcastManager.unregisterReceiver(myBroadcast)
    }

    companion object {
        const val MY_ID = 321
        const val DATA = "DATA"
        const val CHANNEL_ID = "CHANNEL_ID"
    }
}