package com.example.assignment4.ui.screens

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.assignment4.data.model.Expense
import com.example.assignment4.data.model.Occupation
import com.example.assignment4.data.room.AppDatabase
import com.example.assignment4.data.repository.HotelRepository
import com.example.assignment4.databinding.ActivityAddOccupationBinding
import com.example.assignment4.utils.DateUtils
import com.example.assignment4.viewmodel.HotelViewModel
import com.example.assignment4.viewmodel.HotelViewModelFactory
import kotlinx.coroutines.launch

class AddOccupationActivity : AppCompatActivity() {

    // 1. Khai báo biến binding
    private lateinit var binding: ActivityAddOccupationBinding

    private val viewModel: HotelViewModel by viewModels {
        val database = AppDatabase.getDatabase(this)
        val repository = HotelRepository(
            database.clientDao(), database.roomDao(),
            database.occupationDao(), database.expenseDao()
        )
        HotelViewModelFactory(repository)
    }

    private var selectedClientId: Int = -1
    private var selectedRoomId: Int = -1
    private val tempExpenses = mutableListOf<Expense>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 2. Khởi tạo binding và nạp layout
        binding = ActivityAddOccupationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupButtons()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.allClients.collect { clients ->
                val clientNames = clients.map { it.name }
                val adapter = ArrayAdapter(this@AddOccupationActivity, R.layout.simple_spinner_dropdown_item, clientNames)

                // 3. Gọi các View thông qua chữ "binding."
                binding.spClient.adapter = adapter

                if (clients.isNotEmpty()) selectedClientId = clients[0].id
            }
        }

        lifecycleScope.launch {
            viewModel.roomsWithDetails.collect { rooms ->
                val roomDisplays = rooms.map { "Phòng ${it.roomNumber} - ${it.typeName} ($${it.price})" }
                val adapter = ArrayAdapter(this@AddOccupationActivity, R.layout.simple_spinner_dropdown_item, roomDisplays)

                binding.spRoom.adapter = adapter

                if (rooms.isNotEmpty()) selectedRoomId = rooms[0].id
            }
        }
    }

    private fun setupButtons() {
        binding.btnAddExpense.setOnClickListener {
            val desc = binding.edtExpenseDesc.text.toString()
            val amountStr = binding.edtExpenseAmount.text.toString()

            if (desc.isNotEmpty() && amountStr.isNotEmpty()) {
                val expense = Expense(
                    occupationId = 0,
                    describeFee = desc,
                    amount = amountStr.toDouble()
                )
                tempExpenses.add(expense)
                Toast.makeText(this, "Đã thêm $desc ($$amountStr)", Toast.LENGTH_SHORT).show()

                binding.edtExpenseDesc.text.clear()
                binding.edtExpenseAmount.text.clear()
            }
        }

        binding.btnSaveAll.setOnClickListener {
            val checkInStr = binding.edtCheckIn.text.toString()
            val checkOutStr = binding.edtCheckOut.text.toString()

            if (checkInStr.isEmpty() || checkOutStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập ngày!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val checkInLong = DateUtils.convertStringToLong(checkInStr)
            val checkOutLong = DateUtils.convertStringToLong(checkOutStr)

            val newOccupation = Occupation(
                clientId = selectedClientId,
                roomId = selectedRoomId,
                dateTake = checkInLong,
                dateReturn = checkOutLong
            )

            viewModel.saveOccupationAndExpenses(newOccupation, tempExpenses)

            Toast.makeText(this, "Lưu đơn đặt phòng thành công!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}