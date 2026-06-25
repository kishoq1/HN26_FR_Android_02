package com.example.exam.data.repository

import com.example.exam.data.local.Contact
import com.example.exam.data.local.ContactDao
import kotlinx.coroutines.flow.Flow

class ContactRepository(private val contactDao: ContactDao) {
    fun getAllContacts(): Flow<List<Contact>> = contactDao.getAllContacts()

    fun searchContacts(query: String): Flow<List<Contact>> = contactDao.searchContacts(query)

    suspend fun getContactById(id: Int): Contact? = contactDao.getContactById(id)
    suspend fun insert(contact: Contact) = contactDao.insertContact(contact)
    suspend fun update(contact: Contact) = contactDao.updateContact(contact)
    suspend fun delete(contact: Contact) = contactDao.deleteContact(contact)
}