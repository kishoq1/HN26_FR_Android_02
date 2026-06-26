package com.example.assignment4.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "clients")
data class Client(
    @PrimaryKey (autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "gender") val gender : String,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "phoneNumber") val phoneNumber: String
) : Serializable