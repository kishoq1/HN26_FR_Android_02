package com.example.assignment62.model

import java.io.Serializable

data class Playlist(
    val id: Long = System.currentTimeMillis(), // Dùng thời gian hiện tại làm ID ngẫu nhiên
    var name: String,
    val songs: MutableList<Song> = mutableListOf() // Chứa danh sách các bài hát được thêm vào
) : Serializable