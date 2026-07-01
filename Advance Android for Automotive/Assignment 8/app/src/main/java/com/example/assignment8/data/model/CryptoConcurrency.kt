package com.example.assignment8.data.model

data class CryptoCurrency(
    val id: String,
    val name: String,
    val symbol: String,
    val priceUsd: Double,
    val changePercent24Hr: Double
)