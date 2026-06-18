package com.example.assignment7

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionViewModel(private val repository: TransactionRepository) : ViewModel() {

    val allTransactions = repository.allTransactions

    // 1. Logic thêm giao dịch (SALE hoặc REFUND)
    fun addTransaction(type: String, amount: Double, currency: String, holderName: String) {
        viewModelScope.launch {
            val newTransaction = TransactionEntity(
                transactionType = type,
                amount = amount,
                currency = currency,
                holderName = holderName,
                dateTime = System.currentTimeMillis()
            )
            repository.insert(newTransaction)
        }
    }

    // 2. Logic xóa toàn bộ dữ liệu (Clear Batch)
    fun clearBatch() {
        viewModelScope.launch {
            repository.clearBatch()
        }
    }

    // 3. Logic tính tổng doanh thu theo điều kiện (Filter)
    fun calculateRevenue(
        transactions: List<TransactionEntity>,
        filterType: String? = "None",
        filterCurrency: String? = "None",
        filterHolderName: String? = "None",
        filterDate: String? = "None"
    ): Double {
        var filteredList = transactions

        // Lọc theo Transaction Type (SALE / REFUND)
        if (filterType != "None" && !filterType.isNullOrEmpty()) {
            filteredList = filteredList.filter { it.transactionType == filterType }
        }

        if (filterDate != "None" && !filterDate.isNullOrEmpty()) {
            // Tạo công cụ định dạng ngày tháng (Ví dụ: 25/10/2023)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            filteredList = filteredList.filter { transaction ->
                // Ép kiểu mili-giây trong DB thành chuỗi ngày tháng
                val transactionDateString = dateFormat.format(Date(transaction.dateTime))
                // So sánh xem có giống ngày người dùng chọn không
                transactionDateString == filterDate
            }
        }

        // Lọc theo Currency (VND / USD)
        if (filterCurrency != "None" && !filterCurrency.isNullOrEmpty()) {
            filteredList = filteredList.filter { it.currency == filterCurrency }
        }

        // Lọc theo Holder Name
        if (filterHolderName != "None" && !filterHolderName.isNullOrEmpty()) {
            filteredList = filteredList.filter {
                it.holderName.contains(filterHolderName, ignoreCase = true)
            }
        }


        // Tính tổng doanh thu
        var totalRevenue = 0.0
        for (item in filteredList) {
            if (item.transactionType == "SALE") {
                totalRevenue += item.amount
            } else if (item.transactionType == "REFUND") {
                totalRevenue -= item.amount
            }
        }

        return totalRevenue
    }

    // Thêm hàm đồng bộ dữ liệu
    fun syncDataWithServer(transactions: List<TransactionEntity>, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            if (transactions.isEmpty()) {
                onResult(false)
                return@launch
            }
            // Gọi Repository ở luồng nền (tránh đơ UI)
            val success = repository.syncToServer(transactions)
            onResult(success)
        }
    }
}

class TransactionViewModelFactory(private val repository: TransactionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}