package com.example.assignment4.data.model
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity (tableName = "roomtypes")
data class RoomType(
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,

    @ColumnInfo(name = "typeName") val typeName : String,
    @ColumnInfo(name = "facilities") val facilities : String,
    @ColumnInfo(name = "price") val price : Double
) : Serializable