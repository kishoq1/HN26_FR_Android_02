package com.example.assignment4.ui.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.assignment4.data.repository.HotelRepository
import com.example.assignment4.data.room.AppDatabase
import com.example.assignment4.databinding.ActivityReportBinding
import com.example.assignment4.viewmodel.HotelViewModel
import com.example.assignment4.viewmodel.HotelViewModelFactory
import kotlinx.coroutines.launch

class ReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportBinding

    // Khởi tạo ViewModel
    private val viewModel: HotelViewModel by viewModels {
        val database = AppDatabase.getDatabase(this)
        val repository = HotelRepository(
            database.clientDao(), database.roomDao(),
            database.occupationDao(), database.expenseDao()
        )
        HotelViewModelFactory(repository)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCalculate.setOnClickListener {
            val clientIdStr = binding.edtClientId.text.toString()
            val yearStr = binding.edtYear.text.toString()

            if (yearStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập năm!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val year = yearStr.toInt()

            // 1. Tính tổng doanh thu khách sạn theo năm (Task 5)
            lifecycleScope.launch {
                viewModel.getHotelTotalRevenueInYear(year).collect { total ->
                    val displayTotal = total ?: 0.0
                    binding.tvHotelTotal.text = "Tổng doanh thu khách sạn năm $year: $$displayTotal"
                }
            }

            // 2. Tính tổng chi phí của 1 khách hàng nếu có nhập ID (Task 4)
            if (clientIdStr.isNotEmpty()) {
                val clientId = clientIdStr.toInt()
                lifecycleScope.launch {
                    viewModel.getClientTotalExpenseInYear(clientId, year).collect { total ->
                        val displayTotal = total ?: 0.0
                        binding.tvClientTotal.text = "Tổng chi phí của khách ID=$clientId: $$displayTotal"
                    }
                }
            } else {
                binding.tvClientTotal.text = "Vui lòng nhập ID khách để xem chi phí cá nhân"
            }
        }
    }
}