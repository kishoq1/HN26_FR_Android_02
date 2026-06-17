package com.example.assignment62.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.assignment62.model.Playlist
import com.example.assignment62.model.Song

class SharedMusicViewModel : ViewModel() {
    val songs = MutableLiveData<List<Song>>()
    val playlists = MutableLiveData<MutableList<Playlist>>(mutableListOf())
}