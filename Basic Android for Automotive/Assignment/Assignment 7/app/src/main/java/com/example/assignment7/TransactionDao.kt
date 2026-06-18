package com.example.assignment7

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    // 1. Thêm một giao dịch mới (Sale hoặc Refund)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    // 2. Clear Batch (Xóa sạch toàn bộ dữ liệu, reset lại auto-generate ID)
    @Query("DELETE FROM transaction_table")
    suspend fun clearBatch()

    // Lệnh này dùng để reset lại bộ đếm Invoice Number trong SQLite về 0
    @Query("DELETE FROM sqlite_sequence WHERE name='transaction_table'")
    suspend fun resetPrimaryKey()

    // 3. Lấy toàn bộ danh sách giao dịch để tính toán doanh thu (Revenue)
    // Sắp xếp theo thời gian mới nhất lên đầu
    @Query("SELECT * FROM transaction_table ORDER BY dateTime DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

}