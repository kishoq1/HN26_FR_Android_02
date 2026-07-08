package com.example.assignment7.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.assignment7.data.model.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    // tự động emit khi bảng contacts có sự thay đổi (thêm, sửa, xóa)
    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getAllContacts() : Flow<List<Contact>>

    //Tìm kiếm theo tên hoặc SĐT
    @Query("SELECT * FROM contacts WHERE name LIKE '%' || :searchQuery || '%' OR phoneNumber LIKE '%' || :searchQuery || '%'")
    fun searchContacts (searchQuery: String) : Flow<List<Contact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Contact)

    @Update
    suspend fun updateContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)
}