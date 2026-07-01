package com.example.assignment8.ui.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment8.data.model.CryptoCurrency
import com.example.assignment8.R
import androidx.core.graphics.toColorInt

class CryptoAdapter(private var cryptoList: List<CryptoCurrency>) :
    RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<CryptoCurrency>) {
        cryptoList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_crypto, parent, false)
        return CryptoViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val crypto = cryptoList[position]
        holder.tvName.text = crypto.name
        holder.tvSymbol.text = crypto.symbol
        holder.tvPrice.text = "$${crypto.priceUsd}"

        // Đổi màu xanh/đỏ tùy theo phần trăm tăng/giảm
        holder.tvChange.text = "${crypto.changePercent24Hr}%"
        if (crypto.changePercent24Hr >= 0) {
            holder.tvChange.setTextColor("#4CAF50".toColorInt())
        } else {
            holder.tvChange.setTextColor("#F44336".toColorInt())
        }
    }

    override fun getItemCount(): Int = cryptoList.size

    class CryptoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvCoinName)
        val tvSymbol: TextView = itemView.findViewById(R.id.tvCoinSymbol)
        val tvPrice: TextView = itemView.findViewById(R.id.tvCoinPrice)
        val tvChange: TextView = itemView.findViewById(R.id.tvCoinChange)
    }
}