package com.example.assignment4.ui.screens

import android.R
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
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
import java.util.Calendar
import java.util.Locale

class AddOccupationActivity : AppCompatActivity() {

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

        binding = ActivityAddOccupationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupButtons()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.allClients.collect { clients ->
                val clientNames = clients.map { it.name }
                val adapter = ArrayAdapter(this@AddOccupationActivity, android.R.layout.simple_spinner_dropdown_item, clientNames)
                binding.spClient.adapter = adapter

                binding.spClient.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                        selectedClientId = clients[position].id
                    }

                    override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                    }
                }
            }
        }

        // 2. Xử lý Spinner Phòng
        lifecycleScope.launch {
            viewModel.roomsWithDetails.collect { rooms ->
                val roomDisplays = rooms.map { "Phòng ${it.roomNumber} - ${it.typeName} ($${it.price})" }
                val adapter = ArrayAdapter(this@AddOccupationActivity, android.R.layout.simple_spinner_dropdown_item, roomDisplays)
                binding.spRoom.adapter = adapter

                // BẮT SỰ KIỆN KHI NGƯỜI DÙNG CHỌN PHÒNG
                binding.spRoom.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                        selectedRoomId = rooms[position].id
                    }

                    override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                    }
                }
            }
        }
    }

    // Hàm hỗ trợ mở bảng chọn ngày và điền vào ô EditText
    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                editText.setText(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun setupButtons() {
        // Sự kiện mở Lịch khi chạm vào EditText
        binding.edtCheckIn.setOnClickListener {
            showDatePickerDialog(binding.edtCheckIn)
        }

        binding.edtCheckOut.setOnClickListener {
            showDatePickerDialog(binding.edtCheckOut)
        }

        // Sự kiện nút thêm chi phí
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

        // Sự kiện nút lưu toàn bộ
        binding.btnSaveAll.setOnClickListener {
            val checkInStr = binding.edtCheckIn.text.toString()
            val checkOutStr = binding.edtCheckOut.text.toString()

            if (checkInStr.isEmpty() || checkOutStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ngày nhận và trả phòng!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val checkInLong = DateUtils.convertStringToLong(checkInStr)
            val checkOutLong = DateUtils.convertStringToLong(checkOutStr)

            // Kiểm tra logic ngày (Ngày trả phải sau hoặc bằng ngày nhận)
            if (checkOutLong < checkInLong) {
                Toast.makeText(this, "Ngày trả phòng không hợp lệ!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

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