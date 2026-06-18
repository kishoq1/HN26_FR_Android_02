package com.example.assignment7

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: TransactionViewModel

    private lateinit var etAmount: EditText
    private lateinit var etHolderName: EditText
    private lateinit var rgCurrency: RadioGroup
    private lateinit var rbVND: RadioButton
    private lateinit var spinnerTypeFilter: Spinner
    private lateinit var spinnerCurrencyFilter: Spinner
    private lateinit var etNameFilter: EditText
    private lateinit var tvTotalRevenue: TextView
    private lateinit var tvSelectedDate: TextView
    private var selectedFilterDate: String = "None"

    // Biến lưu trữ danh sách giao dịch hiện tại để đẩy lên Server (Sync)
    private var currentTransactions: List<TransactionEntity> = emptyList()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. KHỞI TẠO DATABASE, REPOSITORY & VIEWMODEL
        val database = TransactionDatabase.getDatabase(this)
        val repository = TransactionRepository(database.transactionDao())
        val factory = TransactionViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[TransactionViewModel::class.java]

        // 2. ÁNH XẠ GIAO DIỆN (FIND VIEW BY ID)
        etAmount = findViewById(R.id.etAmount)
        etHolderName = findViewById(R.id.etHolderName)
        rgCurrency = findViewById(R.id.rgCurrency)
        rbVND = findViewById(R.id.rbVND)

        spinnerTypeFilter = findViewById(R.id.spinnerTypeFilter)
        spinnerCurrencyFilter = findViewById(R.id.spinnerCurrencyFilter)
        etNameFilter = findViewById(R.id.etNameFilter)
        tvTotalRevenue = findViewById(R.id.tvTotalRevenue)

        tvSelectedDate = findViewById(R.id.tvSelectedDate)

        val btnSale = findViewById<Button>(R.id.btnSale)
        val btnRefund = findViewById<Button>(R.id.btnRefund)
        val btnClearBatch = findViewById<Button>(R.id.btnClearBatch)
        val btnApplyFilter = findViewById<Button>(R.id.btnApplyFilter)
        val btnSync = findViewById<Button>(R.id.btnSync)
        val btnSelectDate = findViewById<Button>(R.id.btnSelectDate)
        val btnClearDate = findViewById<Button>(R.id.btnClearDate)

        // 3. XỬ LÝ SỰ KIỆN CÁC NÚT BẤM

        // Thêm giao dịch SALE / REFUND
        btnSale.setOnClickListener { processTransaction("SALE") }
        btnRefund.setOnClickListener { processTransaction("REFUND") }

        // Xóa toàn bộ dữ liệu (Clear Batch)
        btnClearBatch.setOnClickListener {
            viewModel.clearBatch()
            Toast.makeText(this, "Batch Cleared!", Toast.LENGTH_SHORT).show()
        }

        // Đồng bộ dữ liệu lên Server (Retrofit)
        btnSync.setOnClickListener {
            Toast.makeText(this, "Syncing...", Toast.LENGTH_SHORT).show()
            viewModel.syncDataWithServer(currentTransactions) { isSuccess ->
                if (isSuccess) {
                    Toast.makeText(this, "Sync Successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Sync Failed (No real server)", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Mở bảng chọn Ngày tháng (Date Picker Dialog)
        btnSelectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                selectedFilterDate = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                tvSelectedDate.text = selectedFilterDate
            }, year, month, day)

            datePickerDialog.show()
        }

        // Xóa bộ lọc Ngày tháng
        btnClearDate.setOnClickListener {
            selectedFilterDate = "None"
            tvSelectedDate.text = "None"
        }

        // Nút Apply Filter - Áp dụng bộ lọc
        btnApplyFilter.setOnClickListener {
            updateRevenueUI(currentTransactions)
        }

        // 4. LẮNG NGHE DỮ LIỆU TỪ DATABASE (FLOW)
        lifecycleScope.launch {
            viewModel.allTransactions.collect { transactionsList ->
                // Cập nhật lại biến lưu trữ để Sync
                currentTransactions = transactionsList
                // Cập nhật lại giao diện hiển thị doanh thu
                updateRevenueUI(transactionsList)
            }
        }
    }

    // CÁC HÀM HỖ TRỢ BÊN NGOÀI
    private fun processTransaction(type: String) {
        val amountStr = etAmount.text.toString()
        val holderName = etHolderName.text.toString()

        if (amountStr.isEmpty() || holderName.isEmpty()) {
            Toast.makeText(this, "Please enter amount and holder name", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountStr.toDouble()
        val currency = if (rbVND.isChecked) "VND" else "USD"

        viewModel.addTransaction(type, amount, currency, holderName)

        etAmount.text.clear()
        etHolderName.text.clear()
        Toast.makeText(this, "$type recorded!", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n")
    private fun updateRevenueUI(transactions: List<TransactionEntity>) {
        val selectedType = spinnerTypeFilter.selectedItem.toString()
        val selectedCurrency = spinnerCurrencyFilter.selectedItem.toString()
        val filterName = etNameFilter.text.toString()

        // Gọi ViewModel tính toán tổng doanh thu dựa trên tất cả các bộ lọc
        val revenue = viewModel.calculateRevenue(
            transactions = transactions,
            filterType = selectedType,
            filterCurrency = selectedCurrency,
            filterHolderName = filterName,
            filterDate = selectedFilterDate
        )

        tvTotalRevenue.text = "TOTAL REVENUE: $revenue"
    }
}