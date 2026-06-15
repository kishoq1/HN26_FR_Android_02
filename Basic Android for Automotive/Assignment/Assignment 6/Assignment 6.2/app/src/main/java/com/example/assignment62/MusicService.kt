package com.example.assignment62

import android.app.PendingIntent
import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class MusicService : MediaSessionService() {
    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()

        // 1. Khai báo thuộc tính: "Đây là ứng dụng phát nhạc, hãy ưu tiên nó!"
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()

        // 2. Khởi tạo ExoPlayer với khả năng chống "cướp" âm thanh
        val player = ExoPlayer.Builder(this)
            .setAudioAttributes(audioAttributes, true) // Tự động xử lý Audio Focus (giảm âm lượng khi có tin nhắn tới...)
            .setHandleAudioBecomingNoisy(true) // Tự động tạm dừng nhạc nếu người dùng rút tai nghe
            .build()

        // Thêm chế độ tự động lặp lại cả danh sách khi phát hết
        player.repeatMode = Player.REPEAT_MODE_ALL

        // 3. Gắn "tai nghe" để bắt lỗi. Nếu nhạc tắt do file hỏng, nó sẽ in dòng chữ màu đỏ ra Logcat
        player.addListener(object : Player.Listener {
            @OptIn(UnstableApi::class)
            override fun onPlayerError(error: PlaybackException) {
                Log.e("MusicService", "Lỗi phát nhạc: ${error.message}", error)
            }
        })

        // Gắn Player vào Session
        mediaSession = MediaSession.Builder(this, player).build()

        val openPlayerIntent = Intent(this, PlayerActivity::class.java)
        val pendingOpenIntent = PendingIntent.getActivity(
            this,
            0,
            openPlayerIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Gắn vào MediaSession
        mediaSession = MediaSession.Builder(this, player)
            .setSessionActivity(pendingOpenIntent) // Dòng này sẽ mở Activity khi bấm vào Notification
            .build()
    }

    // Android hệ thống sẽ gọi hàm này để giao tiếp với Service
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession?.player?.release()
        mediaSession?.release()
        mediaSession = null
        super.onDestroy()
    }
}