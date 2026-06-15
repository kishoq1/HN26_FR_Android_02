package com.example.assignment5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SingerAdapter(
    private val singers: List<Singer>,
    private val onItemClick: (Singer) -> Unit
) : RecyclerView.Adapter<SingerAdapter.SingerViewHolder>() {

    class SingerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvSingerName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_singer, parent, false)
        return SingerViewHolder(view)
    }

    override fun onBindViewHolder(holder: SingerViewHolder, position: Int) {
        val singer = singers[position]
        holder.tvName.text = singer.name
        holder.itemView.setOnClickListener { onItemClick(singer) }
    }

    override fun getItemCount() = singers.size
}