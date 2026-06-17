package com.example.assignment62.service

import android.R
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.example.assignment62.MainActivity
import com.example.assignment62.PlayerActivity
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

class MusicService : MediaSessionService() {
    private var mediaSession: MediaSession? = null

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()

        val audioAttributes = AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()

        val player = ExoPlayer.Builder(this)
            .setAudioAttributes(audioAttributes, true)
            .setHandleAudioBecomingNoisy(true)
            .build()

        player.repeatMode = Player.REPEAT_MODE_ALL

        player.addListener(object : Player.Listener {
            @OptIn(UnstableApi::class)
            override fun onPlayerError(error: PlaybackException) {
                Log.e("MusicService", "Lỗi phát nhạc: ${error.message}", error)
            }
        })

        val openPlayerIntent = Intent(this, PlayerActivity::class.java)
        val pendingOpenIntent = PendingIntent.getActivity(
            this,
            0,
            openPlayerIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // 1. TẠO LỆNH TÙY CHỈNH CHO NÚT "X"
        val closeCommand = SessionCommand("ACTION_CLOSE_APP", Bundle.EMPTY)

        // 2. THIẾT KẾ GIAO DIỆN NÚT "X" (Sẽ hiển thị trên Notification)
        val closeButton = CommandButton.Builder()
            .setDisplayName("Đóng ứng dụng")
            .setIconResId(R.drawable.ic_menu_close_clear_cancel) // Icon dấu X có sẵn của Android
            .setSessionCommand(closeCommand)
            .build()

        // 3. ĐĂNG KÝ NÚT LÊN MEDIASESSION VÀ BẮT SỰ KIỆN
        mediaSession = MediaSession.Builder(this, player)
            .setSessionActivity(pendingOpenIntent)
            .setCustomLayout(listOf(closeButton)) // Ép nút X hiển thị lên layout của thanh thông báo
            .setCallback(object : MediaSession.Callback {

                // Cấp quyền để ứng dụng được phép nhận lệnh Custom Command này
                @OptIn(UnstableApi::class)
                override fun onConnect(
                    session: MediaSession,
                    controller: MediaSession.ControllerInfo
                ): MediaSession.ConnectionResult {
                    val connectionResult = super.onConnect(session, controller)
                    val availableSessionCommands = connectionResult.availableSessionCommands.buildUpon()
                        .add(closeCommand)
                        .build()
                    return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
                        .setAvailableSessionCommands(availableSessionCommands)
                        .build()
                }

                // Xử lý logic khi người dùng bấm vào nút "X" trên thông báo
                override fun onCustomCommand(
                    session: MediaSession,
                    controller: MediaSession.ControllerInfo,
                    customCommand: SessionCommand,
                    args: Bundle
                ): ListenableFuture<SessionResult> {
                    if (customCommand.customAction == "ACTION_CLOSE_APP") {
                        // A. Dừng phát nhạc và dọn dẹp trình phát
                        player.stop()
                        player.clearMediaItems()

                        // B. Bắn tín hiệu "Xóa sổ toàn bộ Activity" tới MainActivity
                        val exitIntent = Intent(this@MusicService, MainActivity::class.java).apply {
                            // FLAG_ACTIVITY_CLEAR_TASK sẽ quét sạch toàn bộ PlayerActivity hay các Fragment đang đè lên nhau
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            putExtra("EXIT_APP", true)
                        }
                        startActivity(exitIntent)

                        // C. Rút thanh Notification và tắt Service
                        stopSelf()
                    }
                    return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
                }
            })
            .build()
    }

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