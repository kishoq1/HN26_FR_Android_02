package com.example.assignment4

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BookAdapter(
    private var bookList: List<Book>,
    private val onItemClick: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCover: ImageView = itemView.findViewById(R.id.imgBookCover)
        val tvTitle: TextView = itemView.findViewById(R.id.tvBookTitle)
        val tvAuthor: TextView = itemView.findViewById(R.id.tvBookAuthor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentBook = bookList[position]

        Glide.with(holder.itemView.context)
            .load(currentBook.imageUrl)
            .placeholder(R.drawable.ic_book) // Hiển thị tạm icon cũ trong lúc chờ mạng load
            .error(R.drawable.ic_book)       // Nếu link mạng lỗi thì hiển thị icon này
            .centerCrop()
            .into(holder.imgCover)

        holder.tvTitle.text = currentBook.title
        holder.tvAuthor.text = currentBook.author

        holder.itemView.setOnClickListener {
            onItemClick(currentBook)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<Book>) {
        bookList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return bookList.size
    }
}