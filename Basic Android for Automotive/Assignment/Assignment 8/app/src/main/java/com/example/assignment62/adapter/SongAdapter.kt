package com.example.assignment62.adapter

import android.annotation.SuppressLint
import android.content.ContentUris
import android.view.LayoutInflater
import android.view.View
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.core.net.toUri
import com.example.assignment62.R
import com.example.assignment62.model.Song

class SongAdapter(
    private var songList: List<Song>,
    private val onItemClick: (Song) -> Unit,
    private val onAddToPlaylistClick: (Song) -> Unit
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvSongTitle)
        val tvArtist: TextView = view.findViewById(R.id.tvSongArtist)
        val imgSongCover: ImageView = view.findViewById(R.id.imgSongCover)
        val btnMoreOptions: ImageView = view.findViewById(R.id.btnMoreOptions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songList[position]
        holder.tvTitle.text = song.title
        holder.tvArtist.text = song.artist

        val sArtworkUri = "content://media/external/audio/albumart".toUri()
        val albumArtUri = ContentUris.withAppendedId(sArtworkUri, song.albumId)

        holder.imgSongCover.setImageURI(albumArtUri)
        if (holder.imgSongCover.drawable == null) {
            holder.imgSongCover.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        // 1. Sự kiện bấm vào dòng nhạc (Phát nhạc bình thường)
        holder.itemView.setOnClickListener { onItemClick(song) }

        // 2. Sự kiện bấm vào nút 3 chấm (Mở Menu con)
        holder.btnMoreOptions.setOnClickListener { view ->
            // Khởi tạo PopupMenu neo vào nút 3 chấm
            val popupMenu = PopupMenu(view.context, holder.btnMoreOptions)

            popupMenu.menu.add(0, 1, 0, "Thêm vào playlist")
            popupMenu.menu.add(0, 2, 0, "Đổi tên")
            popupMenu.menu.add(0, 3, 0, "Xóa")

            // Bắt sự kiện khi người dùng chọn 1 mục
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    1 -> {
                        onAddToPlaylistClick(song)
                        true
                    }
                    2 -> {
                        Toast.makeText(view.context, "Tính năng Đổi tên '${song.title}' đang phát triển!", Toast.LENGTH_SHORT).show()
                        true
                    }
                    3 -> {
                        Toast.makeText(view.context, "Tính năng Xóa '${song.title}' đang phát triển!", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
        setAnimation(holder.itemView, position)
    }

    override fun getItemCount(): Int = songList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newSongs: List<Song>) {
        songList = newSongs
        notifyDataSetChanged()
    }

    private var lastPosition = -1
    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val translateAnim = ObjectAnimator.ofFloat(viewToAnimate, "translationY", 150f, 0f)
            val alphaAnim = ObjectAnimator.ofFloat(viewToAnimate, "alpha", 0f, 1f)

            AnimatorSet().apply {
                playTogether(translateAnim, alphaAnim)
                duration = 400
                start()
            }
        } else if (position < lastPosition) {
            val translateAnim = ObjectAnimator.ofFloat(viewToAnimate, "translationY", -150f, 0f)
            val alphaAnim = ObjectAnimator.ofFloat(viewToAnimate, "alpha", 0f, 1f)

            AnimatorSet().apply {
                playTogether(translateAnim, alphaAnim)
                duration = 400
                start()
            }
        }
        lastPosition = position
    }
}