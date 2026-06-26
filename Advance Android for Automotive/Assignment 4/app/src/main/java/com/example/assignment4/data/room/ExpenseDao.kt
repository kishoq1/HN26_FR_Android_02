package com.example.assignment4.data.room

import androidx.room.Dao
import androidx.room.*
import com.example.assignment4.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense): Long

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expenses WHERE occupation_id = :occupationId")
    fun getExpensesByOccupation(occupationId: Int): Flow<List<Expense>>
}