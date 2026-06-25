package com.example.exam.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exam.R
import com.example.exam.data.model.Contact
import com.example.exam.data.sqlite.DatabaseHelper
import com.example.exam.ui.detail.DetailActivity
import com.example.exam.ui.edit.EditActivity

class MainActivity : AppCompatActivity() {

    private lateinit var rvContacts: RecyclerView
    private lateinit var adapter: ContactAdapter
    private lateinit var dbHelper: DatabaseHelper
    private var contactToDelete: Contact? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.mainToolbar)
        setSupportActionBar(toolbar)

        dbHelper = DatabaseHelper(this)
        rvContacts = findViewById(R.id.rvContacts)
        rvContacts.layoutManager = LinearLayoutManager(this)

        setupAdapter()
    }

    override fun onResume() {
        super.onResume()
        loadContacts()
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
                Toast.makeText(this, "Đã chọn: ${contact.name}. Hãy nhấn icon Thùng Rác để xóa.", Toast.LENGTH_SHORT).show()
            }
        )
        rvContacts.adapter = adapter
    }

    private fun loadContacts(keyword: String = "") {
        val list = dbHelper.getAllContacts(keyword)
        adapter.updateData(list)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                loadContacts(newText ?: "")
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
                    dbHelper.deleteContact(contactToDelete!!.id)
                    Toast.makeText(this, "Đã xóa thành công", Toast.LENGTH_SHORT).show()
                    contactToDelete = null
                    loadContacts()
                } else {
                    Toast.makeText(this, "Vui lòng ấn giữ một danh bạ dưới danh sách để chọn trước", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}