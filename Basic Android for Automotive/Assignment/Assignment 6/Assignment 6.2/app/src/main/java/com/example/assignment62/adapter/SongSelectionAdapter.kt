package com.example.assignment62.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment62.R
import com.example.assignment62.model.Song

class SongSelectionAdapter(private val songList: List<Song>) : RecyclerView.Adapter<SongSelectionAdapter.ViewHolder>() {

    // Dùng Set để lưu bài hát được chọn, giúp tránh trùng lặp dữ liệu
    val selectedSongs = mutableSetOf<Song>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvSelectTitle)
        val tvArtist: TextView = view.findViewById(R.id.tvSelectArtist)
        val checkBox: CheckBox = view.findViewById(R.id.checkboxSong)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song_selectable, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songList[position]
        holder.tvTitle.text = song.title
        holder.tvArtist.text = song.artist

        // Hủy lắng nghe sự kiện cũ để tránh lỗi UI khi RecyclerView tái sử dụng dòng
        holder.checkBox.setOnCheckedChangeListener(null)

        // Đánh dấu trạng thái hiện tại
        holder.checkBox.isChecked = selectedSongs.contains(song)

        // Cập nhật lại danh sách khi người dùng tích/bỏ tích
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedSongs.add(song)
            } else {
                selectedSongs.remove(song)
            }
        }

        // Bấm vào bất kỳ đâu trên dòng cũng sẽ thay đổi trạng thái Checkbox cho tiện lợi
        holder.itemView.setOnClickListener {
            holder.checkBox.isChecked = !holder.checkBox.isChecked
        }
    }

    override fun getItemCount() = songList.size
}