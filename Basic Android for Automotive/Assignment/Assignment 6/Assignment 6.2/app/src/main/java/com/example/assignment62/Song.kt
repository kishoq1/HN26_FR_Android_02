package com.example.assignment62

import java.io.Serializable

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val path: String // Đường dẫn thực tế để MediaPlayer có thể phát nhạc
) : Serializable