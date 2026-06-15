package com.example.assignment62

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedMusicViewModel : ViewModel() {
    // Biến LiveData này sẽ tự động thông báo cho UI mỗi khi dữ liệu bên trong nó thay đổi
    val songs = MutableLiveData<List<Song>>()
}