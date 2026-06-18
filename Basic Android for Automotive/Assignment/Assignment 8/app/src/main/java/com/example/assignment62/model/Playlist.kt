package com.example.assignment62.model

import java.io.Serializable

data class Playlist(
    val id: Long = System.currentTimeMillis(),
    var name: String,
    val songs: MutableList<Song> = mutableListOf()
) : Serializable