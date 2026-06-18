package com.example.assignment62.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.assignment62.model.Playlist
import com.example.assignment62.model.Song
import com.google.android.material.bottomsheet.BottomSheetDialog

class AlbumsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_simple_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewSimple)

        recyclerView.layoutManager = LinearLayoutManager(context)

        val adapter = PlaylistAdapter(emptyList()) { clickedAlbumGroup ->
            showSongsBottomSheet(clickedAlbumGroup.name, clickedAlbumGroup.songs)
        }
        recyclerView.adapter = adapter

        val sharedViewModel = ViewModelProvider(requireActivity())[SharedMusicViewModel::class.java]
        sharedViewModel.songs.observe(viewLifecycleOwner) { songs ->
            val groupedByAlbum = songs.groupBy { it.album }
            val albumPlaylists = groupedByAlbum.map { (albumName, songsOfAlbum) ->
                Playlist(name = albumName, songs = songsOfAlbum.toMutableList())
            }
            adapter.updateData(albumPlaylists.sortedBy { it.name })
        }

        return view
    }

    // Hàm hiển thị danh sách bài hát chi tiết của Album
    private fun showSongsBottomSheet(artistName: String, artistSongs: List<Song>) {
        val dialog = BottomSheetDialog(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_song_list, null)

        val tvDialogTitle = dialogView.findViewById<TextView>(R.id.tvDialogTitle)
        val rvDialogSongs = dialogView.findViewById<RecyclerView>(R.id.rvDialogSongs)

        tvDialogTitle.text = artistName
        rvDialogSongs.layoutManager = LinearLayoutManager(context)

        // Truyền thêm hàm xử lý onAddToPlaylistClick
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

    // BỔ SUNG: Hàm vẽ Hộp thoại chọn Playlist
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
                sharedViewModel.playlists.value = currentPlaylists
                Toast.makeText(requireContext(), "Đã thêm vào ${selectedPlaylist.name}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Bài hát đã có trong Playlist này!", Toast.LENGTH_SHORT).show()
            }
        }
        builder.show()
    }
}