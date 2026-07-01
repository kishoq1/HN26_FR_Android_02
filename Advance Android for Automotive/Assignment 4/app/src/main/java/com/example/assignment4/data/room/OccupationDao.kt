package com.example.assignment4.data.room

import androidx.room.*
import com.example.assignment4.data.model.*
import kotlinx.coroutines.flow.Flow

data class OccupationDetail(
    val occupationId: Int,
    val clientName: String,
    val roomNumber: Int,
    val roomTypeName: String,
    val dateTake: Long,
    val dateReturn: Long,
    val totalExpense: Double
)
@Dao
interface OccupationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOccupation(occupation: Occupation): Long

    @Update
    suspend fun updateOccupation(occupation: Occupation)


    // Task 4: Tổng chi phí của 1 khách hàng
    @Query("""
        SELECT SUM(
            ((occupations.dateReturn - occupations.dateTake) / 86400000) * roomtypes.price 
            + 
            COALESCE((SELECT SUM(amount) FROM expenses WHERE occupation_id = occupations.id), 0.0)
        )
        FROM occupations
        INNER JOIN rooms ON occupations.room_id = rooms.id
        INNER JOIN roomtypes ON rooms.roomtype_id = roomtypes.id
        WHERE occupations.client_id = :clientId 
          AND occupations.dateTake >= :startOfYear 
          AND occupations.dateTake <= :endOfYear
    """)
    fun getClientTotalExpense(clientId: Int, startOfYear: Long, endOfYear: Long): Flow<Double?>

    // Task 5: Tổng doanh thu toàn bộ khách sạn
    @Query("""
        SELECT SUM(
            ((occupations.dateReturn - occupations.dateTake) / 86400000) * roomtypes.price 
            + 
            COALESCE((SELECT SUM(amount) FROM expenses WHERE occupation_id = occupations.id), 0.0)
        )
        FROM occupations
        INNER JOIN rooms ON occupations.room_id = rooms.id
        INNER JOIN roomtypes ON rooms.roomtype_id = roomtypes.id
        WHERE occupations.dateTake >= :startOfYear 
          AND occupations.dateTake <= :endOfYear
    """)
    fun getHotelTotalRevenue(startOfYear: Long, endOfYear: Long): Flow<Double?>

    @Query("""
        SELECT occupations.id AS occupationId, 
               clients.name AS clientName, 
               rooms.roomNumber, 
               roomtypes.typeName AS roomTypeName, 
               occupations.dateTake, 
               occupations.dateReturn,
               COALESCE((SELECT SUM(amount) FROM expenses WHERE occupation_id = occupations.id), 0.0) AS totalExpense
        FROM occupations
        INNER JOIN clients ON occupations.client_id = clients.id
        INNER JOIN rooms ON occupations.room_id = rooms.id
        INNER JOIN roomtypes ON rooms.roomtype_id = roomtypes.id
        ORDER BY occupations.dateTake DESC
    """)
    fun getAllOccupationDetails(): Flow<List<OccupationDetail>>
}