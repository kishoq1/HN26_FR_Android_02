package com.example.assignment4.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.assignment4.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomPhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: RoomPhoto): Long

    @Delete
    suspend fun deletePhoto(photo: RoomPhoto)

    @Query("SELECT * FROM roomphotos WHERE roomtype_id = :typeId")
    fun getPhotosByRoomType(typeId: Int): Flow<List<RoomPhoto>>

    @Query("SELECT * FROM roomphotos WHERE roomtype_id = :typeId AND is_default = 1 LIMIT 1")
    suspend fun getDefaultPhoto(typeId: Int): RoomPhoto?
}