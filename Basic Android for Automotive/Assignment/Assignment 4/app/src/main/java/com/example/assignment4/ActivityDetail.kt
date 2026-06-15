package com.example.assignment4

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val imgCover = findViewById<ImageView>(R.id.imgDetailCover)
        val tvTitle = findViewById<TextView>(R.id.tvDetailTitle)
        val tvAuthor = findViewById<TextView>(R.id.tvDetailAuthor)
        val tvDesc = findViewById<TextView>(R.id.tvDetailDesc)

        val book = intent.getSerializableExtra("EXTRA_BOOK") as? Book

        if (book != null) {
            // Thay dòng imgCover.setImageResource cũ bằng Glide
            Glide.with(this)
                .load(book.imageUrl)
                .placeholder(R.drawable.ic_book)
                .centerCrop()
                .into(imgCover)

            tvTitle.text = book.title
            tvAuthor.text = "Tác giả: ${book.author}"
            tvDesc.text = book.description
        }
    }
}