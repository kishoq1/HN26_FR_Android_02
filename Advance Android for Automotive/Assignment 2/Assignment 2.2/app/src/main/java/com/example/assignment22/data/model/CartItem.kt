package com.example.assignment22.data.model

import java.io.Serializable

data class CartItem(
    val foodItem: FoodItem,
    val quantity: Int = 1
) : Serializable