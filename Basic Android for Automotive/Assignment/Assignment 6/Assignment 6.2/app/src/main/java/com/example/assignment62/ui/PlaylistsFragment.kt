package com.example.assignment62.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment62.MainActivity
import com.example.assignment62.R
import com.example.assignment62.viewmodel.SharedMusicViewModel
import com.example.assignment62.adapter.PlaylistAdapter
import com.example.assignment62.adapter.SongAdapter
import com.example.assignment62.adapter.SongSelectionAdapter
import com.example.assignment62.model.Playlist
import com.example.assignment62.model.Song
import com.google.android.material.bottomsheet.BottomSheetDialog

class PlaylistsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var playlistAdapter: PlaylistAdapter
    private val myPlaylists = mutableListOf<Playlist>()
    private var allSongs = listOf<Song>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_playlists, container, false)

        val btnCreatePlaylist = view.findViewById<View>(R.id.btnCreatePlaylist)
        recyclerView = view.findViewById(R.id.recyclerViewPlaylists)

        playlistAdapter = PlaylistAdapter(myPlaylists) { clickedPlaylist ->
            if (clickedPlaylist.songs.isNotEmpty()) {
                showSongsBottomSheet(clickedPlaylist.name, clickedPlaylist.songs)
            } else {
                Toast.makeText(requireContext(), "Playlist này đang trống!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = playlistAdapter

        btnCreatePlaylist.setOnClickListener {
            showCreatePlaylistDialog()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedViewModel = ViewModelProvider(requireActivity())[SharedMusicViewModel::class.java]

        sharedViewModel.songs.observe(viewLifecycleOwner) { songs ->
            allSongs = songs
        }

        sharedViewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            playlistAdapter.updateData(playlists)
        }
    }

    private fun showCreatePlaylistDialog() {
        // Nạp giao diện tùy chỉnh vừa thiết kế
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_playlist, null)
        val etPlaylistName = dialogView.findViewById<EditText>(R.id.etPlaylistName)
        val rvSelectableSongs = dialogView.findViewById<RecyclerView>(R.id.rvSelectableSongs)

        // Thiết lập Adapter chứa Checkbox cho hộp thoại
        rvSelectableSongs.layoutManager = LinearLayoutManager(requireContext())
        val selectionAdapter = SongSelectionAdapter(allSongs)
        rvSelectableSongs.adapter = selectionAdapter

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Tạo Playlist mới")
        builder.setView(dialogView)

        builder.setPositiveButton("Tạo") { dialog, _ ->
            val playlistName = etPlaylistName.text.toString().trim()
            val selectedSongs = selectionAdapter.selectedSongs.toList()

            if (playlistName.isNotEmpty()) {
                val newPlaylist =
                    Playlist(name = playlistName, songs = selectedSongs.toMutableList())

                // ĐẨY PLAYLIST MỚI LÊN VIEWMODEL
                val sharedViewModel = ViewModelProvider(requireActivity())[SharedMusicViewModel::class.java]
                val currentPlaylists = sharedViewModel.playlists.value ?: mutableListOf()
                currentPlaylists.add(newPlaylist)
                sharedViewModel.playlists.value = currentPlaylists

                Toast.makeText(requireContext(), "Đã tạo: $playlistName (${selectedSongs.size} bài)", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Tên không được để trống!", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Hủy") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun showSongsBottomSheet(artistName: String, artistSongs: List<Song>) {
        val dialog = BottomSheetDialog(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_song_list, null)

        val tvDialogTitle = dialogView.findViewById<TextView>(R.id.tvDialogTitle)
        val rvDialogSongs = dialogView.findViewById<RecyclerView>(R.id.rvDialogSongs)

        tvDialogTitle.text = artistName
        rvDialogSongs.layoutManager = LinearLayoutManager(context)

        // CẬP NHẬT: Truyền thêm hàm xử lý onAddToPlaylistClick
        val songAdapter = SongAdapter(
            songList = artistSongs,
            onItemClick = { clickedSong ->
                val startIndex = artistSongs.indexOf(clickedSong)
                (requireActivity() as MainActivity).playSongs(artistSongs, startIndex)
                dialog.dismiss()
            },
            onAddToPlaylistClick = { selectedSong ->
                showAddToPlaylistDialog(selectedSong)
                dialog.dismiss()
            }
        )

        rvDialogSongs.adapter = songAdapter
        dialog.setContentView(dialogView)
        dialog.show()
    }

    // Hàm vẽ Hộp thoại chọn Playlist
    private fun showAddToPlaylistDialog(song: Song) {
        val sharedViewModel = ViewModelProvider(requireActivity())[SharedMusicViewModel::class.java]
        val currentPlaylists = sharedViewModel.playlists.value ?: mutableListOf()

        if (currentPlaylists.isEmpty()) {
            Toast.makeText(requireContext(), "Bạn chưa có Playlist nào. Hãy tạo mới ở tab Playlists!", Toast.LENGTH_SHORT).show()
            return
        }

        // Lấy ra danh sách các Tên Playlist để in lên Menu
        val playlistNames = currentPlaylists.map { it.name }.toTypedArray()

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Chọn Playlist")
        builder.setItems(playlistNames) { _, which ->
            val selectedPlaylist = currentPlaylists[which]

            // Kiểm tra xem bài hát đã tồn tại trong Playlist này chưa
            if (!selectedPlaylist.songs.any { it.id == song.id }) {
                selectedPlaylist.songs.add(song)

                // Báo cho ViewModel biết dữ liệu đã thay đổi
                sharedViewModel.playlists.value = currentPlaylists

                Toast.makeText(requireContext(), "Đã thêm vào ${selectedPlaylist.name}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Bài hát đã có trong Playlist này!", Toast.LENGTH_SHORT).show()
            }
        }
        builder.show()
    }
}