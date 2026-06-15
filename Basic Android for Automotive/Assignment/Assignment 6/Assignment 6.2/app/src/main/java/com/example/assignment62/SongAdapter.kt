package com.example.assignment62

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongAdapter(
    private var songList: List<Song>,
    private val onItemClick: (Song) -> Unit
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvSongTitle)
        val tvArtist: TextView = view.findViewById(R.id.tvSongArtist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songList[position]
        holder.tvTitle.text = song.title
        holder.tvArtist.text = song.artist

        holder.itemView.setOnClickListener { onItemClick(song) }
    }

    override fun getItemCount(): Int = songList.size

    // Hàm để Fragment gọi khi có dữ liệu nhạc mới
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newSongs: List<Song>) {
        songList = newSongs
        notifyDataSetChanged()
    }
}