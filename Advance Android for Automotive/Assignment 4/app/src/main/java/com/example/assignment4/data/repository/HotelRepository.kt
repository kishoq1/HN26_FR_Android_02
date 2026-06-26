package com.example.assignment4.data.repository

import com.example.assignment4.data.model.*
import com.example.assignment4.data.room.*
import kotlinx.coroutines.flow.Flow

class HotelRepository(
    private val clientDao: ClientDao,
    private val roomDao: RoomDao,
    private val occupationDao: OccupationDao,
    private val expenseDao: ExpenseDao
    // Bạn có thể thêm RoomTypeDao và RoomPhotoDao nếu cần dùng ở màn hình khác
) {

    // 1. Phơi bày các luồng dữ liệu cơ bản (Cho Task 1 và Task 3)
    val allClients: Flow<List<Client>> = clientDao.getAllClients()
    val roomsWithDetails: Flow<List<RoomDetail>> = roomDao.getRoomsWithDetails()

    // 2. Thao tác ghi dữ liệu (Dạy cho Coroutines chạy ngầm)
    suspend fun insertOccupation(occupation: Occupation): Long {
        return occupationDao.insertOccupation(occupation)
    }

    suspend fun insertExpense(expense: Expense) {
        expenseDao.insertExpense(expense)
    }

    // 3. Các hàm thống kê phức tạp (Cho Task 4 và Task 5)
    fun getClientTotalExpense(clientId: Int, startYear: Long, endYear: Long): Flow<Double?> {
        return occupationDao.getClientTotalExpense(clientId, startYear, endYear)
    }

    fun getHotelTotalRevenue(startYear: Long, endYear: Long): Flow<Double?> {
        return occupationDao.getHotelTotalRevenue(startYear, endYear)
    }

    val allOccupationDetails: Flow<List<OccupationDetail>> = occupationDao.getAllOccupationDetails()
}