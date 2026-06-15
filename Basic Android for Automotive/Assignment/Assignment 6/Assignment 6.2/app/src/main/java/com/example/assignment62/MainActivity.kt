package com.example.assignment62

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    // Danh sách lưu trữ các bài hát quét được từ thiết bị
    private var songList = listOf<Song>()

    // 1. Khai báo Launcher để xử lý kết quả trả về khi người dùng bấm "Cho phép" hoặc "Từ chối"
    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        // Kiểm tra xem quyền đọc nhạc đã được cấp chưa
        val readAudioGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions[Manifest.permission.READ_MEDIA_AUDIO] == true
        } else {
            permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
        }

        if (readAudioGranted) {
            loadMusic() // Quyền nhạc OK -> Tải nhạc
        } else {
            Toast.makeText(this, "Ứng dụng cần quyền đọc file để phát nhạc!", Toast.LENGTH_LONG).show()
        }

        // Cảnh báo nhẹ nếu người dùng không cấp quyền thông báo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && permissions[Manifest.permission.POST_NOTIFICATIONS] == false) {
            Toast.makeText(this, "Lưu ý: Bạn sẽ không thấy trình điều khiển nhạc trên thanh thông báo", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val rootView = findViewById<View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        val pagerAdapter = MainPagerAdapter(this)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Playlists"
                1 -> "Artists"
                2 -> "Albums"
                else -> ""
            }
        }.attach()

        // 2. Kích hoạt quy trình kiểm tra quyền ngay khi màn hình vừa khởi tạo xong
        checkPermissions()
    }

    // 3. Hàm kiểm tra phiên bản Android và xin quyền tương ứng
    private fun checkPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        // Chuẩn bị quyền đọc nhạc
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_MEDIA_AUDIO)
            }
            // BỔ SUNG: Chuẩn bị quyền hiển thị thông báo
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        // Thực thi xin quyền nếu danh sách chưa trống
        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionsLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            // Nếu đã có đủ quyền từ trước rồi thì quét nhạc luôn
            loadMusic()
        }
    }

    // 4. Hàm thực thi quét nhạc
    private fun loadMusic() {
        songList = MusicUtils.getAudioFiles(this)

        // In log ra để kiểm tra nhanh dưới Logcat
        Log.d("MusicPlayer", "Đã tìm thấy ${songList.size} bài hát")
        val sharedViewModel = ViewModelProvider(this)[SharedMusicViewModel::class.java]
        sharedViewModel.songs.value = songList

        if (songList.isNotEmpty()) {
            Toast.makeText(this, "Đã tải thành công ${songList.size} bài hát", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Không tìm thấy file nhạc nào trong máy!", Toast.LENGTH_SHORT).show()
        }
    }


}