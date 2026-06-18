package com.example.assignment7

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_table")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val invoiceNumber: Int = 0,

    val transactionType: String,
    val amount: Double,
    val currency: String,
    val holderName: String,
    val dateTime: Long
)