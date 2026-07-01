package com.example.assignment22.data.model

import java.io.Serializable

data class FoodItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double
) : Serializable