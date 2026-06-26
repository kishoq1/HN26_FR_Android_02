package com.example.assignment4.ui.screens


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment4.data.room.AppDatabase
import com.example.assignment4.data.repository.HotelRepository
import com.example.assignment4.databinding.ActivityOccupationListBinding
import com.example.assignment4.ui.adapter.OccupationAdapter
import com.example.assignment4.viewmodel.HotelViewModel
import com.example.assignment4.viewmodel.HotelViewModelFactory
import kotlinx.coroutines.launch

class OccupationListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOccupationListBinding
    private lateinit var adapter: OccupationAdapter

    // Khởi tạo ViewModel
    private val viewModel: HotelViewModel by viewModels {
        val database = AppDatabase.getDatabase(this)
        val repository = HotelRepository(
            database.clientDao(), database.roomDao(),
            database.occupationDao(), database.expenseDao()
        )
        HotelViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Sử dụng View Binding
        binding = ActivityOccupationListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeData()
    }

    private fun setupRecyclerView() {
        adapter = OccupationAdapter()
        binding.rvOccupations.layoutManager = LinearLayoutManager(this)
        binding.rvOccupations.adapter = adapter
    }

    private fun observeData() {
        // Thu thập luồng dữ liệu từ ViewModel và đẩy vào Adapter
        lifecycleScope.launch {
            viewModel.allOccupationDetails.collect { listDetails ->
                adapter.submitList(listDetails)
            }
        }
    }
}