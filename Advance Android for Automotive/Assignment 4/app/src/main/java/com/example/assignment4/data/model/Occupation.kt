package com.example.assignment4.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "occupations", foreignKeys = [
    ForeignKey(
        entity = Client::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("client_id"),
        onDelete = ForeignKey.CASCADE
    ),

    ForeignKey(
        entity = Room::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("room_id"),
        onDelete = ForeignKey.CASCADE
    )
], indices = [Index(value = ["client_id"]), Index(value = ["room_id"])])
data class Occupation(
    @PrimaryKey (autoGenerate = true)
    var id : Int = 0,

    @ColumnInfo(name = "client_id") val clientId : Int,
    @ColumnInfo(name  = "room_id") val roomId : Int,
    @ColumnInfo(name = "dateTake") val dateTake : Long,
    @ColumnInfo(name = "dateReturn") val dateReturn : Long
) : Serializable