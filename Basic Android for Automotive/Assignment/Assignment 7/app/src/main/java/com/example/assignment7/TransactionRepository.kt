package com.example.assignment7

import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {

    // Lấy luồng dữ liệu danh sách giao dịch.
    val allTransactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()

    // Hàm thêm giao dịch
    suspend fun insert(transaction: TransactionEntity) {
        transactionDao.insertTransaction(transaction)
    }

    // Hàm xóa sạch dữ liệu và reset bộ đếm Invoice Number
    suspend fun clearBatch() {
        transactionDao.clearBatch()
        transactionDao.resetPrimaryKey()
    }
    // Đẩy danh sách lên Server
    suspend fun syncToServer(transactions: List<TransactionEntity>): Boolean {
        return try {
            // Gọi hàm sync từ Retrofit
            val response = RetrofitClient.apiService.syncTransactions(transactions)
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}