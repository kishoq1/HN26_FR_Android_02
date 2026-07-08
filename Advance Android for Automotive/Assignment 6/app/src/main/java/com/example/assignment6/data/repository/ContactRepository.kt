package com.example.assignment6.data.repository

import androidx.room.Query
import com.example.assignment6.data.local.ContactDao
import io.reactivex.rxjava3.core.Observable
import com.example.assignment6.data.model.Contact
import io.reactivex.rxjava3.core.Completable

class ContactRepository(private val contactDao: ContactDao) {

    //Nguồn phát dữ liệu danh sách liên hệ
    fun getAllContacts() : Observable<List<Contact>>{
        return contactDao.getAllContacts()
    }

    //Nguồn phát dữ liệu tìm kiếm
    fun searchContacts(query: String) : Observable<List<Contact>>{
        return  contactDao.searchContacts(query)
    }

    //Các thao tác thêm, sửa, xóa
    fun insertContact(contact: Contact) : Completable{
        return contactDao.insertContact(contact)
    }

    fun updateContact(contact: Contact) : Completable{
        return contactDao.updateContact(contact)
    }

    fun deleteContact(contact: Contact) : Completable{
        return contactDao.deleteContact(contact)
    }
}