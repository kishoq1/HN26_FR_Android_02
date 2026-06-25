package com.example.exam

import android.app.Application
import androidx.room.Room
import com.example.exam.data.local.ContactDatabase
import com.example.exam.data.repository.ContactRepository

class ContactApplication : Application() {
    lateinit var database: ContactDatabase
        private set
    lateinit var repository: ContactRepository
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            ContactDatabase::class.java,
            "contact_database"
        ).build()

        repository = ContactRepository(database.contactDao())
    }
}