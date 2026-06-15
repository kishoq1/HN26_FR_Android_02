package com.example.assignment61

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {

    private val CHANNEL_ID = "music_channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Liên kết với giao diện XML
        setContentView(R.layout.activity_main)

        // Ánh xạ nút bấm từ XML
        val btnShowNotification = findViewById<Button>(R.id.btnShowNotification)

        btnShowNotification.setOnClickListener {
            if (checkPermission()) {
                showNotification()
            }
        }

        // Tạo Channel cho Android 8+
        createNotificationChannel()
    }

    private fun showNotification() {
        // 1. Khởi tạo 2 RemoteViews
        val collapsedView = RemoteViews(packageName, R.layout.layout_notification_small)
        val expandedView = RemoteViews(packageName, R.layout.layout_notification_large)

        // 2. Gắn sự kiện click cho các nút trên giao diện Thu gọn (Small)
        collapsedView.setOnClickPendingIntent(R.id.btnPrev, getPendingIntent("ACTION_PREV"))
        collapsedView.setOnClickPendingIntent(R.id.btnPlay, getPendingIntent("ACTION_PLAY"))
        collapsedView.setOnClickPendingIntent(R.id.btnNext, getPendingIntent("ACTION_NEXT"))
        collapsedView.setOnClickPendingIntent(R.id.btnClose, getPendingIntent("ACTION_CLOSE"))

        // 3. Gắn sự kiện click cho các nút trên giao diện Mở rộng (Large)
        expandedView.setOnClickPendingIntent(R.id.btnPrev, getPendingIntent("ACTION_PREV"))
        expandedView.setOnClickPendingIntent(R.id.btnPlay, getPendingIntent("ACTION_PLAY"))
        expandedView.setOnClickPendingIntent(R.id.btnNext, getPendingIntent("ACTION_NEXT"))

        // 4. Build Notification
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setCustomContentView(collapsedView) // Layout mặc định
            .setCustomBigContentView(expandedView) // Layout mở rộng
            .setStyle(NotificationCompat.DecoratedCustomViewStyle()) // Ép hệ thống vẽ mũi tên mở rộng
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true) // Tránh bị vuốt tắt (phải ấn nút Close)
            .build()

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(this).notify(1, notification)
        }
    }

    // Hàm tiện ích tạo PendingIntent
    private fun getPendingIntent(actionName: String): PendingIntent {
        val intent = Intent(this, NotificationReceiver::class.java).apply {
            action = actionName
        }
        // Sử dụng FLAG_IMMUTABLE theo tiêu chuẩn bảo mật mới nhất
        return PendingIntent.getBroadcast(this, actionName.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE)
    }

    // Bắt buộc phải tạo Channel từ Android 8.0 trở lên
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Music Player", NotificationManager.IMPORTANCE_HIGH)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    // Kiểm tra và xin quyền gửi thông báo (Android 13+)
    private fun checkPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
                return false
            }
        }
        return true
    }
}