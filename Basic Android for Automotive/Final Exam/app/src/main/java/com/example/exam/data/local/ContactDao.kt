package com.example.exam.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    // Đọc contact từ Room database, sắp xếp theo alphabet
    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getAllContacts(): Flow<List<Contact>>

    @Query("SELECT * FROM contacts WHERE name LIKE '%' || :searchQuery || '%' ORDER BY name ASC")
    fun searchContacts(searchQuery: String): Flow<List<Contact>>

    @Query("SELECT * FROM contacts WHERE id = :id LIMIT 1")
    suspend fun getContactById(id: Int): Contact?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Contact) : Long

    @Update
    suspend fun updateContact(contact: Contact) : Int

    @Delete
    suspend fun deleteContact(contact: Contact) : Int
}