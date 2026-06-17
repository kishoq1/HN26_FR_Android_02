package com.example.assignment62.adapter

import android.annotation.SuppressLint
import android.content.ContentUris
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.core.net.toUri
import com.example.assignment62.R
import com.example.assignment62.model.Playlist

class PlaylistAdapter(
    private var playlistList: List<Playlist>,
    private val onItemClick: (Playlist) -> Unit
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvPlaylistName)
        val tvTrackCount: TextView = view.findViewById(R.id.tvTrackCount)
        // Ánh xạ ImageView ảnh bìa Playlist
        val imgPlaylistCover: ImageView = view.findViewById(R.id.imgPlaylistCover)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlistList[position]
        holder.tvName.text = playlist.name
        holder.tvTrackCount.text = "${playlist.songs.size} tracks"

        // BỔ SUNG LOGIC: Tự động lấy ảnh của bài hát đầu tiên làm ảnh đại diện cho Playlist
        if (playlist.songs.isNotEmpty()) {
            val firstSong = playlist.songs[0] // Lấy bài hát vị trí đầu tiên
            val sArtworkUri = "content://media/external/audio/albumart".toUri()
            val albumArtUri = ContentUris.withAppendedId(sArtworkUri, firstSong.albumId)

            holder.imgPlaylistCover.setImageURI(albumArtUri)
            holder.imgPlaylistCover.setPadding(0, 0, 0, 0) // Xóa padding 12dp mặc định để ảnh lấp đầy khung
        } else {
            // Nếu playlist chưa có nhạc, hiển thị icon bộ sưu tập mặc định và khôi phục padding cho cân đối
            holder.imgPlaylistCover.setImageResource(android.R.drawable.ic_menu_gallery)
            val density = holder.itemView.context.resources.displayMetrics.density
            val paddingInDp = (12 * density).toInt() // Quy đổi 12dp ra Pixel tương ứng với màn hình thiết bị
            holder.imgPlaylistCover.setPadding(paddingInDp, paddingInDp, paddingInDp, paddingInDp)
        }

        holder.itemView.setOnClickListener { onItemClick(playlist) }
    }

    override fun getItemCount(): Int = playlistList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newPlaylists: List<Playlist>) {
        playlistList = newPlaylists
        notifyDataSetChanged()
    }
}