package com.inaki.firstkotlinapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.inaki.firstkotlinapp.databinding.ActivityMain2Binding

class MainActivity2: AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding

    private val broadcastManager: LocalBroadcastManager by lazy {
        LocalBroadcastManager.getInstance(baseContext)
    }

    // we are creating our file to save data in shared preferences
    // this data is stored locally in android device memory
    private val sharedPreferences: SharedPreferences by lazy {
        baseContext.getSharedPreferences("MY_SHARED_PREFS", MODE_PRIVATE)
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
            // An editor will be needed to write data in share preferences
            sharedPreferences.edit().apply {
                // saving our data into shared preferences as key-value
                putString("EMAIL_KEY", binding.textView.text.toString())
            }.apply()

            // here you are accessing the value saved in share preferences
            // this can be assign to any local variable
            val mString = sharedPreferences.getString("EMAIL_KEY", null)

            // this is to clear the whole shared preferences file
            sharedPreferences.edit().clear().apply()

            // this is to remove an specific key-value in shared preferences
            sharedPreferences.edit().remove("EMAIL_KEY").apply()

            // here we are accessing the full list of keys in shared preferences
            val allKeys = sharedPreferences.all.keys

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