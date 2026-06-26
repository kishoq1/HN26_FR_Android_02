package com.example.assignment4.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "rooms", foreignKeys = [
    ForeignKey(
        entity = RoomType::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("roomtype_id"),
        onDelete = ForeignKey.CASCADE
    )
], indices = [Index(value = ["roomtype_id"])]
)
data class Room(
    @PrimaryKey (autoGenerate = true)
    var id : Int = 0,

    @ColumnInfo(name = "roomtype_id") val roomTypeId : Int,
    @ColumnInfo(name = "roomNumber") val roomNumber : Int
): Serializable