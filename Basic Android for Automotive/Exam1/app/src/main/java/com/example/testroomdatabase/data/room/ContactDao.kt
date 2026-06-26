package com.example.testroomdatabase.data.room


import androidx.room.*
import com.example.testroomdatabase.data.model.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    // Lấy toàn bộ danh sách, trả về Flow để tự động quan sát dữ liệu
    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getAllContacts(): Flow<List<Contact>>

    // Tìm kiếm, cũng trả về Flow
    @Query("SELECT * FROM contacts WHERE name LIKE '%' || :keyword || '%' ORDER BY name ASC")
    fun searchContacts(keyword: String): Flow<List<Contact>>

    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun getContactById(id: Int): Contact?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Contact): Long

    @Update
    suspend fun updateContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("DELETE FROM contacts WHERE id = :id")
    suspend fun deleteContactById(id: Int)
}