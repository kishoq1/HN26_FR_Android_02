package com.example.assignment6.di

import android.content.Context
import com.example.assignment6.data.local.AppDatabase
import com.example.assignment6.data.local.ContactDao
import com.example.assignment6.data.repository.ContactRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) : AppDatabase{
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideContactDao(appDatabase: AppDatabase) : ContactDao{
        return appDatabase.contactDao()
    }

    @Provides
    @Singleton
    fun provideContactRepository(contactDao: ContactDao) : ContactRepository{
        return ContactRepository(contactDao)
    }
}