package com.example.assignment332

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.assignment332.ACTION_START_BROADCAST") {

            Toast.makeText(context, "App B: Đã nhận Broadcast!", Toast.LENGTH_SHORT).show()

            val mainIntent = Intent(context, MainActivity::class.java)
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(mainIntent)
        }
    }
}