package com.example.assignment4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.assignment4.data.model.Expense
import com.example.assignment4.data.model.Occupation
import com.example.assignment4.data.repository.HotelRepository
import com.example.assignment4.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HotelViewModel(private val repository: HotelRepository) : ViewModel() {

    // Danh sách dữ liệu tự động cập nhật lên UI
    val allClients = repository.allClients
    val roomsWithDetails = repository.roomsWithDetails

    // Hàm lưu thông tin đặt phòng và chi phí (Dùng cho Form Task 1)
    fun saveOccupationAndExpenses(occupation: Occupation, expenses: List<Expense>) {
        viewModelScope.launch {
            val newOccupationId = repository.insertOccupation(occupation).toInt()

            expenses.forEach { expense ->
                val expenseWithFk = expense.copy(occupationId = newOccupationId)
                repository.insertExpense(expenseWithFk)
            }
        }
    }

    // Task 4: Tổng chi phí của một khách hàng trong năm
    fun getClientTotalExpenseInYear(clientId: Int, year: Int): Flow<Double?> {
        val startOfYear = DateUtils.getStartOfYearTimestamp(year)
        val endOfYear = DateUtils.getEndOfYearTimestamp(year)
        return repository.getClientTotalExpense(clientId, startOfYear, endOfYear)
    }

    // Task 5: Tổng doanh thu khách sạn theo năm
    fun getHotelTotalRevenueInYear(year: Int): Flow<Double?> {
        val startOfYear = DateUtils.getStartOfYearTimestamp(year)
        val endOfYear = DateUtils.getEndOfYearTimestamp(year)
        return repository.getHotelTotalRevenue(startOfYear, endOfYear)
    }

    val allOccupationDetails = repository.allOccupationDetails
}

// Bắt buộc phải có Factory để truyền Repository vào ViewModel
class HotelViewModelFactory(private val repository: HotelRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HotelViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HotelViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}