package com.example.assignment4.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment4.data.model.Client
import com.example.assignment4.databinding.ItemClientBinding

class ClientAdapter : RecyclerView.Adapter<ClientAdapter.ViewHolder>() {

    private var clients = emptyList<Client>()

    fun submitList(newList: List<Client>) {
        clients = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemClientBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(client: Client) {
            binding.tvClientName.text = "ID: ${client.id} - ${client.name}"
            binding.tvClientPhone.text = "SĐT: ${client.phoneNumber}"
            binding.tvClientInfo.text = "${client.gender} - ${client.country}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemClientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(clients[position])
    }

    override fun getItemCount(): Int = clients.size
}