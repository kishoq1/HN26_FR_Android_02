package com.example.assignment61

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "ACTION_PREV" -> Toast.makeText(context, "Clicked: Previous", Toast.LENGTH_SHORT).show()
            "ACTION_PLAY" -> Toast.makeText(context, "Clicked: Play/Pause", Toast.LENGTH_SHORT).show()
            "ACTION_NEXT" -> Toast.makeText(context, "Clicked: Next", Toast.LENGTH_SHORT).show()
            "ACTION_CLOSE" -> {
                Toast.makeText(context, "Notification Closed", Toast.LENGTH_SHORT).show()
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(1)
            }
        }
    }
}