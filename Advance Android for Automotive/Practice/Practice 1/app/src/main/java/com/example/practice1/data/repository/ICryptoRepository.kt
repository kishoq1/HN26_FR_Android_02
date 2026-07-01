package com.example.practice1.data.repository

import com.example.practice1.data.model.CryptoCurrency
import kotlinx.coroutines.flow.Flow

interface ICryptoRepository {
    fun getCryptoList() : Flow<List<CryptoCurrency>>
}