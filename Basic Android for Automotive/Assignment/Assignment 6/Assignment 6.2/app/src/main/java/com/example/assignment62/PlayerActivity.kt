package com.example.assignment62

import android.content.ComponentName
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture

class PlayerActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvArtist: TextView
    private lateinit var btnPlay: ImageView
    private lateinit var seekBar: SeekBar

    private var mediaControllerFuture: ListenableFuture<MediaController>? = null
    private var mediaController: MediaController? = null

    // Bộ đếm để cập nhật thanh tiến trình mỗi giây
    private val handler = Handler(Looper.getMainLooper())
    private val updateProgressAction = object : Runnable {
        override fun run() {
            updateProgress()
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        tvTitle = findViewById(R.id.tvPlayerTitle)
        tvArtist = findViewById(R.id.tvPlayerArtist)
        btnPlay = findViewById(R.id.btnPlayerPlay)
        seekBar = findViewById(R.id.seekBar)

        val btnPrev = findViewById<ImageView>(R.id.btnPlayerPrev)
        val btnNext = findViewById<ImageView>(R.id.btnPlayerNext)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        btnBack.setOnClickListener { finish() }

        // Bắt sự kiện thao tác bằng MediaController
        btnPlay.setOnClickListener {
            mediaController?.let {
                if (it.isPlaying) it.pause() else it.play()
            }
        }
        btnNext.setOnClickListener { mediaController?.seekToNextMediaItem() }
        btnPrev.setOnClickListener { mediaController?.seekToPreviousMediaItem() }

        // Xử lý sự kiện kéo thả SeekBar
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaController?.seekTo(progress.toLong())
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun onStart() {
        super.onStart()
        // 1. Tạo kết nối tới MusicService khi mở màn hình
        val sessionToken = SessionToken(this, ComponentName(this, MusicService::class.java))
        mediaControllerFuture = MediaController.Builder(this, sessionToken).buildAsync()

        mediaControllerFuture?.addListener({
            mediaController = mediaControllerFuture?.get()

            // Cập nhật giao diện lần đầu tiên
            updateUI()

            // 2. Lắng nghe mọi thay đổi từ Player (Tự động nhảy khi hết bài, đổi trạng thái...)
            mediaController?.addListener(object : Player.Listener {
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    updateUI() // Đổi tên bài hát khi chuyển bài
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    updatePlayPauseButton(isPlaying)
                    if (isPlaying) {
                        handler.post(updateProgressAction)
                    } else {
                        handler.removeCallbacks(updateProgressAction)
                    }
                }
            })

            // Kích hoạt chạy SeekBar nếu nhạc đang phát
            if (mediaController?.isPlaying == true) {
                handler.post(updateProgressAction)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    // Hàm cập nhật Thông tin bài hát
    private fun updateUI() {
        mediaController?.currentMediaItem?.mediaMetadata?.let { metadata ->
            tvTitle.text = metadata.title ?: "Unknown Title"
            tvArtist.text = metadata.artist ?: "Unknown Artist"
        }
        updatePlayPauseButton(mediaController?.isPlaying == true)
        updateProgress()
    }

    // Hàm cập nhật Nút Play/Pause
    private fun updatePlayPauseButton(isPlaying: Boolean) {
        val icon = if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
        btnPlay.setImageResource(icon)
    }

    // Hàm cập nhật SeekBar
    private fun updateProgress() {
        mediaController?.let {
            val duration = it.duration
            val currentPos = it.currentPosition
            if (duration > 0) {
                seekBar.max = duration.toInt()
                seekBar.progress = currentPos.toInt()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        // Giải phóng kết nối và dừng đếm giờ khi thoát màn hình để tiết kiệm pin
        handler.removeCallbacks(updateProgressAction)
        mediaControllerFuture?.let { MediaController.releaseFuture(it) }
        mediaController = null
    }
}