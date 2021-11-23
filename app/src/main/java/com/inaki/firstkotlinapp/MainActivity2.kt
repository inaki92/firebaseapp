package com.inaki.firstkotlinapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.inaki.firstkotlinapp.databinding.ActivityMain2Binding

class MainActivity2: AppCompatActivity() {

    // We have a variable that will be initialized in onCreate for the view binding
    // you need to add buildFeatures in app build.gradle
    private lateinit var binding: ActivityMain2Binding

    // Initializing our broadcast manager with lazy
    // lazy variable are going to be initialized the first time you try to access it
    private val broadcastManager: LocalBroadcastManager by lazy {
        LocalBroadcastManager.getInstance(baseContext)
    }

    // we are creating our file to save data in shared preferences
    // this data is stored locally in android device memory
    private val sharedPreferences: SharedPreferences by lazy {
        baseContext.getSharedPreferences("MY_SHARED_PREFS", MODE_PRIVATE)
    }

    private val notManager: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(baseContext)
    }

    private val myBroadcast = MyBroadcast {
        Log.d("MainActivity2", it)
        val notificationBuilder = Notification.createNotification(baseContext, it, "body").build()
        notManager.notify(MY_ID, notificationBuilder)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Using view binding in order to avoid the findViewById
        // we call inflate and we pass the layoutInflater
        binding = ActivityMain2Binding.inflate(layoutInflater)

        // we set the content for our view referencing to the root layout
        // this will avoid the use of R.layout.activity_main
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        // here we register our broadcast
        broadcastManager.registerReceiver(myBroadcast, MyBroadcast.intentFilter)
    }

    override fun onResume() {
        super.onResume()

        // We create our notification channel
        Notification.createNotificationChannel(baseContext)

        // We read the String passed from the first activity
        val myEmail = intent.getStringExtra(DATA) ?: ""

        // Here we construct the Intent that will be used to send the broadcast
        val mIntent = Intent().apply {
            action = MyBroadcast.MY_ACTION
            putExtra(MyBroadcast.MY_EXTRA, myEmail)
        }

        binding.button2.setOnClickListener {
            // An editor will be needed to write data in share preferences
            sharedPreferences.edit().apply {
                // saving our data into shared preferences as key-value
                putString(EMAIL_KEY, myEmail)
            }.apply()

            // here we are accessing the full list of keys in shared preferences
            val allKeys = sharedPreferences.all.keys

            // sending the broadcast using our local broadcast manager
            broadcastManager.sendBroadcast(mIntent)
        }

        binding.removeData.setOnClickListener {
            // everytime we click thi button we are going to remove the data in shared preferences
            removeMyEmail(EMAIL_KEY)
        }

        binding.retrieveData.setOnClickListener {
            // everytime we click thi button we get the data from shared preferences
            // this data is retrieve depending on the key you are passing
            binding.textView.text = getDataFromSharedPreferences(EMAIL_KEY)
        }
    }

    private fun deleteAllSharePreferencesFile() {
        // this is to clear the whole shared preferences file
        sharedPreferences.edit().clear().apply()
    }

    private fun removeMyEmail(key: String) {
        // this is to remove an specific key-value in shared preferences
        sharedPreferences.edit().remove(key).apply()
    }

    private fun getDataFromSharedPreferences(key: String): String {
        // here you are accessing the value saved in share preferences
        // this can be assign to any local variable
        return sharedPreferences.getString(key, null) ?: ""
    }

    override fun onStop() {
        super.onStop()
        // we need to register the broadcast receivers when on stop happens
        broadcastManager.unregisterReceiver(myBroadcast)
    }

    companion object {
        const val MY_ID = 321
        const val DATA = "DATA"
        const val EMAIL_KEY = "EMAIL_KEY"
        const val CHANNEL_ID = "CHANNEL_ID"
    }
}