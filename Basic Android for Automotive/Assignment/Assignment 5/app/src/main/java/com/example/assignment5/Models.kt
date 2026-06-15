package com.example.assignment5

import java.io.Serializable

data class Song(
    val title: String,
    val duration: String
) : Serializable

data class Singer(
    val name: String,
    val songs: List<Song>
) : Serializable