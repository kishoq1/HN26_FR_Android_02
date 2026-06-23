package com.example.assignment1.data.local

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import com.example.assignment1.data.model.Contact
import androidx.core.database.sqlite.transaction

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "contact_database.db"
        const val TABLE_CONTACTS = "contacts"

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PHONE = "phoneNumber"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE $TABLE_CONTACTS("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_NAME TEXT,"
                + "$COLUMN_PHONE TEXT)")
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    // Hàm lấy tất cả danh bạ
    @SuppressLint("Range")
    fun getAllContacts(): List<Contact> {
        val contactList = ArrayList<Contact>()
        val selectQuery = "SELECT * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val contact = Contact(
                    id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    phoneNumber = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE))
                )
                contactList.add(contact)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return contactList
    }

    // Hàm thêm danh sách liên hệ (dùng để tạo dữ liệu giả)
    fun insertContacts(contacts: List<Contact>) {
        val db = this.writableDatabase
        db.beginTransaction()
        try {
            for (contact in contacts) {
                val values = ContentValues()
                values.put(COLUMN_NAME, contact.name)
                values.put(COLUMN_PHONE, contact.phoneNumber)
                db.insert(TABLE_CONTACTS, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()

            db.close()
        }
    }
}