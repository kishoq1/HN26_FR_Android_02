package com.example.exam.data.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.exam.data.model.Contact
import com.example.exam.utils.*

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_CONTACT ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_NAME TEXT,"
                + "$COLUMN_PHONE TEXT,"
                + "$COLUMN_EMAIL TEXT,"
                + "$COLUMN_AVATAR BLOB)")
        db?.execSQL(createTable)
        insertDummyData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACT")
        onCreate(db)
    }

    private fun insertDummyData(db: SQLiteDatabase?) {
        val dummyContacts = listOf(
            arrayOf("Kimberly Lee", "(650) 555-1367", "kimberly.lee@gmail.com"),
            arrayOf("Mindy Russell", "(650) 555-9462", "mindy.russell@gmail.com"),
            arrayOf("Sean Andersen", "(650) 555-1111", "sean.andersen@gmail.com")
        )
        for (contact in dummyContacts) {
            val values = ContentValues().apply {
                put(COLUMN_NAME, contact[0])
                put(COLUMN_PHONE, contact[1])
                put(COLUMN_EMAIL, contact[2])
                put(COLUMN_AVATAR, null as ByteArray?)
            }
            db?.insert(TABLE_CONTACT, null, values)
        }
    }

    fun addContact(contact: Contact): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, contact.name)
            put(COLUMN_PHONE, contact.phone)
            put(COLUMN_EMAIL, contact.email)
            put(COLUMN_AVATAR, contact.avatar)
        }
        val success = db.insert(TABLE_CONTACT, null, values)
        db.close()
        return success
    }

    fun getAllContacts(keyword: String = ""): ArrayList<Contact> {
        val contactList = ArrayList<Contact>()
        val db = this.readableDatabase
        val query = if (keyword.isEmpty()) {
            "SELECT * FROM $TABLE_CONTACT ORDER BY $COLUMN_NAME ASC"
        } else {
            "SELECT * FROM $TABLE_CONTACT WHERE $COLUMN_NAME LIKE '%$keyword%' ORDER BY $COLUMN_NAME ASC"
        }

        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val contact = Contact(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                    email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    avatar = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_AVATAR))
                )
                contactList.add(contact)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return contactList
    }

    fun getContactById(id: Int): Contact? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_CONTACT WHERE $COLUMN_ID = ?", arrayOf(id.toString()))
        var contact: Contact? = null
        if (cursor.moveToFirst()) {
            contact = Contact(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                avatar = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_AVATAR))
            )
        }
        cursor.close()
        db.close()
        return contact
    }

    fun updateContact(contact: Contact): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, contact.name)
            put(COLUMN_PHONE, contact.phone)
            put(COLUMN_EMAIL, contact.email)
            put(COLUMN_AVATAR, contact.avatar)
        }
        val success = db.update(TABLE_CONTACT, values, "$COLUMN_ID=?", arrayOf(contact.id.toString()))
        db.close()
        return success
    }

    fun deleteContact(id: Int): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_CONTACT, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return success
    }
}