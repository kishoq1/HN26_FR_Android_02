package com.example.assignment7.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment7.data.model.Contact
import com.example.assignment7.data.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    // 1. Luồng Flow chứa từ khóa tìm kiếm (Khởi tạo là chuỗi rỗng)
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // 2. Luồng Flow chứa trạng thái sắp xếp (true = A-Z, false = Z-A)
    private val _isSortAscending = MutableStateFlow(true)
    val isSortAscending: StateFlow<Boolean> = _isSortAscending

    // 3. Luồng Flow gốc lấy dữ liệu từ Room Database
    private val _contactsFromDb = repository.getAllContacts()

    val contacts: StateFlow<List<Contact>> = combine(
        _contactsFromDb,
        _searchQuery,
        _isSortAscending
    ) { dbContacts, query, isAsc ->

        // Bước 1: Lọc dữ liệu theo từ khóa (Tìm theo tên hoặc số điện thoại)
        val filteredList = if (query.isBlank()) {
            dbContacts
        } else {
            dbContacts.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.phoneNumber.contains(query)
            }
        }

        // Bước 2: Sắp xếp danh sách đã lọc
        if (isAsc) {
            filteredList.sortedBy { it.name.lowercase() }
        } else {
            filteredList.sortedByDescending { it.name.lowercase() }
        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // --- CÁC HÀM XỬ LÝ SỰ KIỆN TỪ UI ---

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun toggleSortOrder() {
        _isSortAscending.value = !_isSortAscending.value
    }

    fun addContact(name: String, phoneNumber: String) {
        viewModelScope.launch {
            val newContact = Contact(name = name.trim(), phoneNumber = phoneNumber)
            repository.insertContact(newContact)
        }
    }

    fun updateContact(contact: Contact) {
        viewModelScope.launch {
            repository.updateContact(contact)
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            repository.deleteContact(contact)
        }
    }
}