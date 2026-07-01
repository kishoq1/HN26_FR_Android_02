package com.example.practice1.data.repository

import com.example.practice1.data.model.CryptoCurrency
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class CryptoRepositoryImpl @Inject constructor() : ICryptoRepository  {
    override fun getCryptoList(): Flow<List<CryptoCurrency>> = flow {
        delay(1500.milliseconds)

        val mockData = listOf(
            CryptoCurrency("1", "Bitcoin", "BTC", 65432.10, 2.5),
            CryptoCurrency("2", "Ethereum", "ETH", 3456.78, -1.2),
            CryptoCurrency("3", "Binance Coin", "BNB", 605.20, 0.8),
            CryptoCurrency("4", "Solana", "SOL", 145.50, 5.4),
            CryptoCurrency("5", "Cardano", "ADA", 0.45, -0.5)
        )

        emit(mockData)
    }
}