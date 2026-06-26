package com.example.assignment4.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment4.data.room.OccupationDetail
import com.example.assignment4.databinding.ItemOccupationBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OccupationAdapter : RecyclerView.Adapter<OccupationAdapter.ViewHolder>() {

    // Danh sách dữ liệu
    private var occupations = emptyList<OccupationDetail>()

    fun submitList(newList: List<OccupationDetail>) {
        occupations = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemOccupationBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(detail: OccupationDetail) {
            binding.tvClientName.text = detail.clientName
            binding.tvRoomInfo.text = "Phòng: ${detail.roomNumber} (${detail.roomTypeName})"

            // Ép định dạng tiền tệ
            binding.tvTotalExpenses.text = "Phí phát sinh: $${detail.totalExpense}"

            // Chuyển đổi Timestamp (Long) về chuỗi ngày tháng (String)
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dateIn = sdf.format(Date(detail.dateTake))
            val dateOut = sdf.format(Date(detail.dateReturn))
            binding.tvDates.text = "$dateIn - $dateOut"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOccupationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(occupations[position])
    }

    override fun getItemCount(): Int = occupations.size
}