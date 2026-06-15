package com.example.assignment4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var bookAdapter: BookAdapter
    private lateinit var originalBookList: List<Book>
    private lateinit var currentDisplayList: List<Book>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewBooks)
        val searchView = findViewById<SearchView>(R.id.searchViewBooks)
        val btnSortTitle = findViewById<Button>(R.id.btnSortTitle)
        val btnSortAuthor = findViewById<Button>(R.id.btnSortAuthor)

        recyclerView.layoutManager = LinearLayoutManager(this)

        originalBookList = arrayListOf(
            Book("Clean Code", "Robert C. Martin", "https://covers.openlibrary.org/b/id/8259431-L.jpg", "Sách gối đầu giường về viết code sạch và dễ bảo trì."),
            Book("The Pragmatic Programmer", "Andrew Hunt", "https://covers.openlibrary.org/b/id/6401032-L.jpg", "Hành trình từ một thợ code thành một lập trình viên thực thụ."),
            Book("Design Patterns", "Erich Gamma", "https://covers.openlibrary.org/b/id/13147551-L.jpg", "Nền tảng về các mẫu thiết kế phần mềm kinh điển."),
            Book("Refactoring", "Martin Fowler", "https://covers.openlibrary.org/b/id/9557434-L.jpg", "Cải thiện thiết kế của mã nguồn đã có sẵn."),
            Book("Kotlin in Action", "Dmitry Jemerov", "https://covers.openlibrary.org/b/id/10574103-L.jpg", "Sách chuẩn nhất để làm chủ ngôn ngữ Kotlin."),
            Book("Effective Java", "Joshua Bloch", "https://covers.openlibrary.org/b/id/8666579-L.jpg", "Những best practices không thể bỏ qua trong thế giới Java."),
            Book("Clean Architecture", "Robert C. Martin", "https://covers.openlibrary.org/b/id/8447814-L.jpg", "Bí quyết thiết kế kiến trúc phần mềm bền vững."),
            Book("Grokking Algorithms", "Aditya Bhargava", "https://covers.openlibrary.org/b/id/8372671-L.jpg", "Cấu trúc dữ liệu và giải thuật được giải thích bằng hình ảnh."),
            Book("Head First Design Patterns", "Eric Freeman", "https://covers.openlibrary.org/b/id/10188040-L.jpg", "Học Design Pattern theo cách trực quan và dễ hiểu nhất."),
            Book("Android Programming", "Bill Phillips", "https://covers.openlibrary.org/b/id/10398698-L.jpg", "Hướng dẫn toàn diện về lập trình Android từ Big Nerd Ranch.")
        )
        currentDisplayList = ArrayList(originalBookList)

        bookAdapter = BookAdapter(currentDisplayList) { clickedBook ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("EXTRA_BOOK", clickedBook)
            startActivity(intent)
        }
        recyclerView.adapter = bookAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val query = newText?.lowercase() ?: ""

                currentDisplayList = if (query.isEmpty()) {
                    originalBookList
                } else {
                    originalBookList.filter {
                        it.title.lowercase().contains(query) ||
                                it.author.lowercase().contains(query)
                    }
                }

                bookAdapter.updateData(currentDisplayList)
                return true
            }
        })

        btnSortTitle.setOnClickListener {
            currentDisplayList = currentDisplayList.sortedBy { it.title }
            bookAdapter.updateData(currentDisplayList)
        }

        btnSortAuthor.setOnClickListener {
            currentDisplayList = currentDisplayList.sortedBy { it.author }
            bookAdapter.updateData(currentDisplayList)
        }
    }
}