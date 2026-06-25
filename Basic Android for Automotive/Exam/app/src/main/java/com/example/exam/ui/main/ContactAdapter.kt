package com.example.exam.ui.main

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.exam.R
import com.example.exam.data.model.Contact

class ContactAdapter(
    private var contactList: ArrayList<Contact>,
    private val onItemClick: (Contact) -> Unit,
    private val onLongClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val imgAvatar: ImageView = itemView.findViewById(R.id.imgAvatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.tvName.text = contact.name

        // Hiển thị Avatar
        if (contact.avatar != null) {
            val bitmap = BitmapFactory.decodeByteArray(contact.avatar, 0, contact.avatar!!.size)
            holder.imgAvatar.setImageBitmap(bitmap)
        } else {
            holder.imgAvatar.setImageResource(R.mipmap.ic_launcher)
        }

        holder.itemView.setOnClickListener { onItemClick(contact) }

        holder.itemView.setOnLongClickListener {
            onLongClick(contact)
            true
        }
    }

    override fun getItemCount(): Int = contactList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: ArrayList<Contact>) {
        contactList = newList
        notifyDataSetChanged()
    }
}