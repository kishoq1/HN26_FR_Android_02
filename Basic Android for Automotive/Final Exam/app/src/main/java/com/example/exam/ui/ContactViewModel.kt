package com.example.exam.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exam.data.local.Contact
import com.example.exam.data.repository.ContactRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ContactViewModel(private val repository: ContactRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // Lắng nghe thay đổi từ Database và Search Query
    @OptIn(ExperimentalCoroutinesApi::class)
    val contacts: StateFlow<List<Contact>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) repository.getAllContacts()
            else repository.searchContacts(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchQuery(query: String) { _searchQuery.value = query }

    fun addContact(name: String, phone: String, email: String, avatarUri: String? = null) {
        viewModelScope.launch {
            repository.insert(Contact(name = name, phoneNumber = phone, email = email, avatarUri = avatarUri))
        }
    }

    fun updateContact(contact: Contact) {
        viewModelScope.launch {
            repository.update(contact)
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            repository.delete(contact)
        }
    }

    suspend fun getContactById(id: Int): Contact? {
        return repository.getContactById(id)
    }
}