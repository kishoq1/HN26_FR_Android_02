package com.example.assignment7.data.repository

import com.example.assignment7.data.local.ContactDao
import com.example.assignment7.data.model.Contact
import kotlinx.coroutines.flow.Flow

class ContactRepository(private val contactDao: ContactDao) {

    fun getAllContacts(): Flow<List<Contact>> {
        return contactDao.getAllContacts()
    }

    fun searchContacts(query: String): Flow<List<Contact>> {
        return contactDao.searchContacts(query)
    }

    // Các hàm thao tác dữ liệu được đánh dấu là suspend
    suspend fun insertContact(contact: Contact) {
        contactDao.insertContact(contact)
    }

    suspend fun updateContact(contact: Contact) {
        contactDao.updateContact(contact)
    }

    suspend fun deleteContact(contact: Contact) {
        contactDao.deleteContact(contact)
    }
}