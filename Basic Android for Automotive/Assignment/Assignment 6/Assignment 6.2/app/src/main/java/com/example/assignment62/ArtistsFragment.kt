package com.example.assignment62

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.ComponentName
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken

class ArtistsFragment : Fragment() {

    private lateinit var adapter: SongAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Tiếp tục tái sử dụng giao diện dùng chung
        return inflater.inflate(R.layout.fragment_music_list, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewSongs)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Khởi tạo Adapter và tinh chỉnh lại dòng thông báo (Toast)
        val sharedViewModel = ViewModelProvider(requireActivity())[SharedMusicViewModel::class.java]


        adapter = SongAdapter(emptyList()) { clickedSong ->
            val songs = sharedViewModel.songs.value ?: emptyList()
            val startIndex = songs.indexOf(clickedSong)

            // 1. Tạo "Vé kết nối" đến MusicService
            val sessionToken = SessionToken(requireContext(), ComponentName(requireContext(), MusicService::class.java))

            // 2. Kết nối bất đồng bộ tới Service
            val controllerFuture = MediaController.Builder(requireContext(), sessionToken).buildAsync()

            controllerFuture.addListener({
                val controller = controllerFuture.get()

                // 3. Đóng gói dữ liệu thành chuẩn MediaItem của Google
                val mediaItems = songs.map { song ->
                    MediaItem.Builder()
                        .setMediaId(song.id.toString())
                        .setUri(song.path) // Đường dẫn URI mà ta đã lấy từ MediaStore
                        .setMediaMetadata(
                            MediaMetadata.Builder()
                                .setTitle(song.title)
                                .setArtist(song.artist)
                                .build()
                        )
                        .build()
                }

                // 4. Bơm cả danh sách vào ExoPlayer, chọn bài bắt đầu và Phát!
                controller.setMediaItems(mediaItems, startIndex, 0L)
                controller.prepare()
                controller.play()

                // 5. Chuyển sang màn hình PlayerActivity
                startActivity(Intent(requireContext(), PlayerActivity::class.java))

            }, ContextCompat.getMainExecutor(requireContext())) // Chạy trên luồng chính (UI Thread)
        }

        recyclerView.adapter = adapter
        // Lắng nghe sự thay đổi dữ liệu
        sharedViewModel.songs.observe(viewLifecycleOwner) { songs ->
            // Ở dự án thực tế, đoạn này sẽ được dùng để gom nhóm các bài hát theo tên Tác giả (Artist).
            // Ví dụ: songs.groupBy { it.artist }
            adapter.updateData(songs)
        }
    }
}