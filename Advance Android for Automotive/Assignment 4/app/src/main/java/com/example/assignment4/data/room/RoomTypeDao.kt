package com.example.assignment4.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.assignment4.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomTypeDao {

    @Query("SELECT * FROM roomtypes ORDER BY typeName ASC")
    fun getAllRoomTypes() : Flow<List<RoomType>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoomType(roomType : RoomType): Long

    @Update
    suspend fun updateRoomType(roomType : RoomType)

    @Delete
    suspend fun deleteRoomType(roomType: RoomType)
}