package com.example.assignment4.ui.screens

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment4.data.room.AppDatabase
import com.example.assignment4.data.repository.HotelRepository
import com.example.assignment4.databinding.ActivityClientListBinding
import com.example.assignment4.ui.adapter.ClientAdapter
import com.example.assignment4.viewmodel.HotelViewModel
import com.example.assignment4.viewmodel.HotelViewModelFactory
import kotlinx.coroutines.launch

class ClientListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientListBinding
    private lateinit var adapter: ClientAdapter

    private val viewModel: HotelViewModel by viewModels {
        val database = AppDatabase.getDatabase(this)
        val repository = HotelRepository(database.clientDao(), database.roomDao(), database.occupationDao(), database.expenseDao())
        HotelViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ClientAdapter()
        binding.rvClients.layoutManager = LinearLayoutManager(this)
        binding.rvClients.adapter = adapter

        lifecycleScope.launch {
            viewModel.allClients.collect { list ->
                adapter.submitList(list)
            }
        }
    }
}