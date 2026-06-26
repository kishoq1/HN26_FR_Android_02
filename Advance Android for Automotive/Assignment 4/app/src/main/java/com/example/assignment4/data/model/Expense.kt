package com.example.assignment4.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "expenses", foreignKeys = [
    ForeignKey(
        entity = Occupation::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("occupation_id"),
        onDelete = ForeignKey.CASCADE
    )
], indices = [Index(value = ["occupation_id"])])
data class Expense(
    @PrimaryKey (autoGenerate = true)
    var id : Int = 0,

    @ColumnInfo (name = "occupation_id") val occupationId : Int,
    @ColumnInfo (name = "describeFee") val describeFee : String,
    @ColumnInfo(name = "amount") val amount : Double,
) : Serializable