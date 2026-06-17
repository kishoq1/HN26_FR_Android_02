package com.example.assignment62

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.common.util.concurrent.ListenableFuture
import androidx.core.net.toUri
import com.example.assignment62.adapter.MainPagerAdapter
import com.example.assignment62.model.Song
import com.example.assignment62.service.MusicService
import com.example.assignment62.utils.MusicUtils
import com.example.assignment62.viewmodel.SharedMusicViewModel

class MainActivity : AppCompatActivity() {

    private var songList = listOf<Song>()
    private var mediaController: MediaController? = null
    private var controllerFuture: ListenableFuture<MediaController>? = null

    private lateinit var layoutMiniPlayer: View
    private lateinit var tvMiniTitle: TextView
    private lateinit var btnMiniPlay: ImageView
    private lateinit var imgMiniCover: ImageView

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val readAudioGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions[Manifest.permission.READ_MEDIA_AUDIO] == true
        } else {
            permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
        }

        if (readAudioGranted) {
            loadMusic()
        } else {
            Toast.makeText(this, "Ứng dụng cần quyền đọc file để phát nhạc!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.getBooleanExtra("EXIT_APP", false)) {
            finishAndRemoveTask()
            return
        }
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        layoutMiniPlayer = findViewById(R.id.miniPlayerLayout)
        tvMiniTitle = findViewById(R.id.tvMiniTitle)
        btnMiniPlay = findViewById(R.id.btnMiniPlay)
        // Yêu cầu có ImageView với ID imgMiniCover trong LinearLayout của thanh Mini Player
        imgMiniCover = findViewById(R.id.imgMiniCover)

        layoutMiniPlayer.setOnClickListener {
            if (mediaController?.currentMediaItem != null) {
                startActivity(Intent(this, PlayerActivity::class.java))
            }
        }

        btnMiniPlay.setOnClickListener {
            mediaController?.let {
                if (it.isPlaying) it.pause() else it.play()
            }
        }

        val rootView = findViewById<View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        viewPager.adapter = MainPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Playlists"
                1 -> "Artists"
                2 -> "Albums"
                else -> ""
            }
        }.attach()

        checkPermissions()
    }

    override fun onStart() {
        super.onStart()
        val sessionToken = SessionToken(this, ComponentName(this, MusicService::class.java))
        controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()

        controllerFuture?.addListener({
            val controller = controllerFuture?.get()
            mediaController = controller

            if (controller != null) {
                updateMiniPlayerUI(controller)

                controller.addListener(object : Player.Listener {
                    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                        updateMiniPlayerUI(controller)
                    }
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        updateMiniPlayPauseButton(isPlaying)
                    }
                })
            }
        }, ContextCompat.getMainExecutor(this))
    }

    @SuppressLint("SetTextI18n")
    private fun updateMiniPlayerUI(controller: MediaController) {
        val currentItem = controller.currentMediaItem
        if (currentItem != null) {
            layoutMiniPlayer.visibility = View.VISIBLE

            val title = currentItem.mediaMetadata.title ?: "Unknown Title"
            val artist = currentItem.mediaMetadata.artist ?: "Unknown Artist"
            tvMiniTitle.text = "$title - $artist"

            // ĐỔI CÁCH LẤY ẢNH: Ưu tiên dùng artworkUri trước
            val artworkUri = currentItem.mediaMetadata.artworkUri
            if (artworkUri != null) {
                imgMiniCover.setImageURI(artworkUri)
            } else {
                // Đề phòng trường hợp file có mảng byte mà không có Uri
                val artworkData = currentItem.mediaMetadata.artworkData
                if (artworkData != null) {
                    val bitmap = BitmapFactory.decodeByteArray(artworkData, 0, artworkData.size)
                    imgMiniCover.setImageBitmap(bitmap)
                } else {
                    imgMiniCover.setImageResource(android.R.drawable.ic_menu_gallery)
                }
            }

            updateMiniPlayPauseButton(controller.isPlaying)
        } else {
            layoutMiniPlayer.visibility = View.GONE
        }
    }

    private fun updateMiniPlayPauseButton(isPlaying: Boolean) {
        val icon = if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
        btnMiniPlay.setImageResource(icon)
    }

    fun playSongs(songs: List<Song>, startIndex: Int) {
        val controller = mediaController
        if (controller == null) return

        // Khai báo đường dẫn gốc tới kho ảnh của Android
        val sArtworkUri = "content://media/external/audio/albumart".toUri()

        val mediaItems = songs.map { song ->
            // Nối albumId để ra đường dẫn ảnh của bài hát này
            val albumArtUri = android.content.ContentUris.withAppendedId(sArtworkUri, song.albumId)

            MediaItem.Builder()
                .setMediaId(song.id.toString())
                .setUri(song.path)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(song.title)
                        .setArtist(song.artist)
                        .setArtworkUri(albumArtUri) // BỔ SUNG: Truyền thẳng đường dẫn ảnh vào Player
                        .build()
                )
                .build()
        }

        controller.setMediaItems(mediaItems, startIndex, 0L)
        controller.prepare()
        controller.play()

        startActivity(Intent(this, PlayerActivity::class.java))
    }
    override fun onStop() {
        super.onStop()
        controllerFuture?.let { MediaController.releaseFuture(it) }
        mediaController = null
    }

    private fun checkPermissions() {
        // Rút gọn điều kiện xin quyền
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val req = mutableListOf<String>()
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) req.add(Manifest.permission.READ_MEDIA_AUDIO)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) req.add(Manifest.permission.POST_NOTIFICATIONS)
            if (req.isNotEmpty()) requestPermissionsLauncher.launch(req.toTypedArray()) else loadMusic()
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionsLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
            } else {
                loadMusic()
            }
        }
    }

    private fun loadMusic() {
        songList = MusicUtils.getAudioFiles(this)
        ViewModelProvider(this)[SharedMusicViewModel::class.java].songs.value = songList
    }
}