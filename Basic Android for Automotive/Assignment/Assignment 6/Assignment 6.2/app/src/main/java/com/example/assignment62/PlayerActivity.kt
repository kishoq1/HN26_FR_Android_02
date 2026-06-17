package com.example.assignment62

import android.content.ComponentName
import android.graphics.BitmapFactory
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
import com.example.assignment62.service.MusicService
import com.google.common.util.concurrent.ListenableFuture

class PlayerActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvArtist: TextView
    private lateinit var btnPlay: ImageView
    private lateinit var seekBar: SeekBar
    private lateinit var imgPlayerCover: ImageView

    private var mediaControllerFuture: ListenableFuture<MediaController>? = null
    private var mediaController: MediaController? = null

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

        // Yêu cầu có ImageView với ID imgPlayerCover trong layout activity_player.xml
        imgPlayerCover = findViewById(R.id.imgPlayerCover)

        val btnPrev = findViewById<ImageView>(R.id.btnPlayerPrev)
        val btnNext = findViewById<ImageView>(R.id.btnPlayerNext)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        btnBack.setOnClickListener { finish() }

        btnPlay.setOnClickListener {
            mediaController?.let {
                if (it.isPlaying) it.pause() else it.play()
            }
        }
        btnNext.setOnClickListener { mediaController?.seekToNextMediaItem() }
        btnPrev.setOnClickListener { mediaController?.seekToPreviousMediaItem() }

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
        val sessionToken = SessionToken(this, ComponentName(this, MusicService::class.java))
        mediaControllerFuture = MediaController.Builder(this, sessionToken).buildAsync()

        mediaControllerFuture?.addListener({
            mediaController = mediaControllerFuture?.get()
            updateUI()

            mediaController?.addListener(object : Player.Listener {
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    updateUI()
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

            if (mediaController?.isPlaying == true) {
                handler.post(updateProgressAction)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun updateUI() {
        mediaController?.currentMediaItem?.mediaMetadata?.let { metadata ->
            tvTitle.text = metadata.title ?: "Unknown Title"
            tvArtist.text = metadata.artist ?: "Unknown Artist"

            // ĐỔI CÁCH LẤY ẢNH: Dùng artworkUri thay vì artworkData
            val artworkUri = metadata.artworkUri
            if (artworkUri != null) {
                imgPlayerCover.setImageURI(artworkUri)
            } else {
                val artworkData = metadata.artworkData
                if (artworkData != null) {
                    val bitmap = BitmapFactory.decodeByteArray(artworkData, 0, artworkData.size)
                    imgPlayerCover.setImageBitmap(bitmap)
                } else {
                    imgPlayerCover.setImageResource(android.R.drawable.ic_menu_gallery)
                }
            }
        }
        updatePlayPauseButton(mediaController?.isPlaying == true)
        updateProgress()
    }

    private fun updatePlayPauseButton(isPlaying: Boolean) {
        val icon = if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
        btnPlay.setImageResource(icon)
    }

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
        handler.removeCallbacks(updateProgressAction)
        mediaControllerFuture?.let { MediaController.releaseFuture(it) }
        mediaController = null
    }
}