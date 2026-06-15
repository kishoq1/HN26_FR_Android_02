package com.example.assignment4

import java.io.Serializable

data class Book(
    val title : String,
    val author : String,
    val imageUrl: String,
    val description : String
) : Serializable