package com.example.assignment1.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment1.R
import com.example.assignment1.data.local.DatabaseHelper
import com.example.assignment1.data.model.Contact
import com.example.assignment1.databinding.ActivityMainBinding
import com.example.assignment1.ui.adapter.ContactAdapter
import com.example.assignment1.ui.viewmodel.ContactViewModel
import com.example.assignment1.ui.viewmodel.ContactViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel : ContactViewModel
    private lateinit var adapter : ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Khởi tạo Data Binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        // 2. Khởi tạo Database và ViewModel
        val dbHelper = DatabaseHelper(this)
        val factory = ContactViewModelFactory(dbHelper)
        viewModel = ViewModelProvider(this, factory)[ContactViewModel::class.java]

        binding.viewModel = viewModel

        // 3.Thiết lập RecyclerView
        adapter = ContactAdapter()
        binding.recyclerViewContacts.adapter = adapter
        binding.recyclerViewContacts.layoutManager = LinearLayoutManager(this)

        // 4.Quan sát danh sách contacts để cập nhật lên UI
        viewModel.contacts.observe(this) { contactList ->
            adapter.submitList(contactList)
        }

        // 5. Chuẩn bị dữ liệu cho Database
        prePopulateDatabase(dbHelper)
    }

    private fun prePopulateDatabase(dbHelper : DatabaseHelper){
        lifecycleScope.launch(Dispatchers.IO) {
            if(dbHelper.getAllContacts().isEmpty()){
                val fakeContacts = listOf(
                    Contact(name = "Nguyễn Văn A", phoneNumber = "0901234567"),
                    Contact(name = "Trần Thị B", phoneNumber = "0987654321"),
                    Contact(name = "Lê Văn C", phoneNumber = "0912345678"),
                    Contact(name = "Phạm Thị D", phoneNumber = "0933445566"),
                    Contact(name = "Hoàng Văn E", phoneNumber = "0944556677")
                )
                dbHelper.insertContacts(fakeContacts)
            }
        }
    }
}