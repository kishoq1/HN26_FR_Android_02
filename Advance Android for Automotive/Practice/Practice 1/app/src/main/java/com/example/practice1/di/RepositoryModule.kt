package com.example.practice1.di

import com.example.practice1.data.repository.CryptoRepositoryImpl
import com.example.practice1.data.repository.ICryptoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindCryptoRepository(
        cryptoRepositoryImpl: CryptoRepositoryImpl
    ) : ICryptoRepository
}