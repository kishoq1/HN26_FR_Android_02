package com.example.assignment1.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment1.data.local.DatabaseHelper
import com.example.assignment1.data.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class ContactViewModel(private val dbHelper : DatabaseHelper)  : ViewModel() {

    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts : LiveData<List<Contact>> get() = _contacts

    private val _downloadProgress = MutableLiveData(0)
    val downloadProgress : LiveData<Int> get() = _downloadProgress

    private val _isDownloading = MutableLiveData(false)
    val isDownloading : LiveData<Boolean> get() = _isDownloading

    private val _isDownloadComplete = MutableLiveData(false)
    val isDownloadComplete : LiveData<Boolean> get() = _isDownloadComplete

    fun startDownloadingContacts(){
        _isDownloading.value = true
        _isDownloadComplete.value = false

        viewModelScope.launch(Dispatchers.IO) {
            for(i in 1..100){
                delay(30.milliseconds)
                _downloadProgress.postValue(i)
            }

            val data = dbHelper.getAllContacts()
            _contacts.postValue(data)

            _isDownloading.postValue(false)
            _isDownloadComplete.postValue(true)
        }
    }
}