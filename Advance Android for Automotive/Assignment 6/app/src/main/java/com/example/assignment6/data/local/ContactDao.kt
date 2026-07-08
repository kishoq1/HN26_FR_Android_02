package com.example.assignment6.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.assignment6.data.model.Contact
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
interface ContactDao {
    // tự động emit khi bảng contacts có sự thay đổi (thêm, sửa, xóa)
    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getAllContacts() : Observable<List<Contact>>

    //Tìm kiếm theo tên hoặc SĐT
    @Query("SELECT * FROM contacts WHERE name LIKE '%' || :searchQuery || '%' OR phoneNumber LIKE '%' || :searchQuery || '%'")
    fun searchContacts (searchQuery: String) : Observable<List<Contact>>

    //Trả về Completable cho các thao tác không cần xuất dữ liệu (thành công hoặc thất bại)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContact(contact: Contact): Completable

    @Update
    fun updateContact(contact: Contact): Completable

    @Delete
    fun deleteContact(contact: Contact): Completable
}