package com.example.assignment4.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.assignment4.data.model.*
import kotlinx.coroutines.flow.Flow

data class RoomDetail(
    val id : Int,
    val roomNumber : Int,
    val typeName : String,
    val price : Double,
    val facilities : String
)

@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(room: Room): Long

    @Update
    suspend fun updateRoom(room: Room)

    @Delete
    suspend fun deleteRoom(room: Room)

    // Truy vấn kết hợp lấy tên loại phòng và tiện nghi
    @Query("""
        SELECT rooms.id, rooms.roomNumber, roomtypes.typeName, roomtypes.price, roomtypes.facilities 
        FROM rooms 
        INNER JOIN roomtypes ON rooms.roomtype_id = roomtypes.id 
        ORDER BY rooms.roomNumber ASC
    """)
    fun getRoomsWithDetails(): Flow<List<RoomDetail>>
}