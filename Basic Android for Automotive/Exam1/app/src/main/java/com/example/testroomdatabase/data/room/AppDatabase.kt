package com.example.testroomdatabase.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.testroomdatabase.data.model.Contact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Contact::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ContactDB"
                )
                    .addCallback(RoomDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class RoomDatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Chèn dữ liệu mẫu trên luồng nền (Background thread)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = database.contactDao()
                        dao.insertContact(Contact(name = "Kimberly Lee", phone = "(650) 555-1367", email = "kimberly.lee@gmail.com"))
                        dao.insertContact(Contact(name = "Mindy Russell", phone = "(650) 555-9462", email = "mindy.russell@gmail.com"))
                        dao.insertContact(Contact(name = "Sean Andersen", phone = "(650) 555-1111", email = "sean.andersen@gmail.com"))
                    }
                }
            }
        }
    }
}