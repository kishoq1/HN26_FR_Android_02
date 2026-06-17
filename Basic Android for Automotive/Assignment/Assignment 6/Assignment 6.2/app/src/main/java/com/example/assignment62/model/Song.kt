package com.example.assignment62.model

import java.io.Serializable

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val path: String,
    val albumId: Long,
    val album: String = "Unknown Album"
) : Serializable