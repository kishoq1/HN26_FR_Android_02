package com.example.practice1.data.model

data class CryptoCurrency(
    val id : String,
    val name: String,
    val symbol : String,
    val priceUsd : Double,
    val changePercent24Hr : Double
)