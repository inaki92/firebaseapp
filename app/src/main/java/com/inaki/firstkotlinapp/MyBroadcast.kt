package com.inaki.firstkotlinapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast

class MyBroadcast(
    private val myFunc: ((String) -> Unit)
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "Notification sent", Toast.LENGTH_LONG).show()
        myFunc.invoke(intent.getStringExtra(MY_EXTRA) ?: "")
    }

    companion object {
        const val MY_EXTRA = "MY_EXTRA"
        const val MY_ACTION = "MY_ACTION"
        val intentFilter = IntentFilter(MY_ACTION)
    }
}