package com.example.testroomdatabase.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testroomdatabase.R
import com.example.testroomdatabase.data.model.Contact
import com.example.testroomdatabase.data.room.AppDatabase
import com.example.testroomdatabase.data.room.ContactDao
import com.example.testroomdatabase.ui.detail.DetailActivity
import com.example.testroomdatabase.ui.edit.EditActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var rvContacts: RecyclerView
    private lateinit var adapter: ContactAdapter
    private lateinit var contactDao: ContactDao
    private var contactToDelete: Contact? = null

    // Quản lý tác vụ quan sát Flow để có thể hủy khi tìm kiếm từ khóa mới
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.mainToolbar)
        setSupportActionBar(toolbar)

        contactDao = AppDatabase.getDatabase(this).contactDao()
        rvContacts = findViewById(R.id.rvContacts)
        rvContacts.layoutManager = LinearLayoutManager(this)

        setupAdapter()

        // Chỉ cần gọi quan sát 1 lần, Flow sẽ tự đẩy dữ liệu mới lên khi DB thay đổi
        observeContacts()
    }

    private fun setupAdapter() {
        adapter = ContactAdapter(ArrayList(),
            onItemClick = { contact ->
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("CONTACT_DATA", contact)
                startActivity(intent)
            },
            onLongClick = { contact ->
                contactToDelete = contact
                Toast.makeText(this, "Đã chọn: ${contact.name}. Hãy nhấn Thùng Rác để xóa.", Toast.LENGTH_SHORT).show()
            }
        )
        rvContacts.adapter = adapter
    }

    private fun observeContacts(keyword: String = "") {
        searchJob?.cancel() // Hủy bộ thu thập cũ trước khi tìm từ khóa mới
        searchJob = lifecycleScope.launch {
            val flow = if (keyword.isEmpty()) contactDao.getAllContacts() else contactDao.searchContacts(keyword)

            // Lắng nghe Flow
            flow.collect { list ->
                adapter.updateData(ArrayList(list))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                observeContacts(newText ?: "")
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                startActivity(Intent(this, EditActivity::class.java))
                true
            }
            R.id.action_delete -> {
                if (contactToDelete != null) {
                    // Mọi thao tác ghi/xóa phải chạy trong Coroutine
                    lifecycleScope.launch {
                        contactDao.deleteContact(contactToDelete!!)
                        contactToDelete = null
                        Toast.makeText(this@MainActivity, "Đã xóa thành công", Toast.LENGTH_SHORT).show()
                        // Không cần gọi loadContacts() nữa vì Flow sẽ tự cập nhật giao diện!
                    }
                } else {
                    Toast.makeText(this, "Vui lòng ấn giữ danh bạ để chọn trước", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}