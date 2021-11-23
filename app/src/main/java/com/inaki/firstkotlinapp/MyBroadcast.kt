package com.inaki.firstkotlinapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast

class MyBroadcast(
    // This function has to be passed at the moment you are instantiating the broadcast receiver
    // Here we are accepting a function which will be 'Unit' and will return a String in the lambda
    private val myFunc: ((String) -> Unit)
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // HERE we are receiving the broadcast information using the Intent
        Toast.makeText(context, "Notification sent", Toast.LENGTH_LONG).show()

        // This line will invoke or trigger the function that wwe passed in the constructor
        myFunc.invoke(intent.getStringExtra(MY_EXTRA) ?: "")
    }

    companion object {
        const val MY_EXTRA = "MY_EXTRA"
        const val MY_ACTION = "MY_ACTION"
        val intentFilter = IntentFilter(MY_ACTION)
    }
}