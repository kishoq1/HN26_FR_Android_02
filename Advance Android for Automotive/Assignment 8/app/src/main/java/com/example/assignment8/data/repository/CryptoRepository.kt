package com.example.assignment8.data.repository

import com.example.assignment8.data.model.CryptoCurrency
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

//khai báo cho Hilt biết cách tạo ra class này bằng "@Inject constructor" đặt trước constructor của class
class CryptoRepository @Inject constructor() {

    // Trả về một Flow chứa danh sách coin để mô phỏng luồng dữ liệu bất đồng bộ
    fun getCryptoList(): Flow<List<CryptoCurrency>> = flow {
        delay(1500.milliseconds)

        // Tạo dữ liệu giả
        val mockData = listOf(
            CryptoCurrency("1", "Bitcoin", "BTC", 65432.10, 2.5),
            CryptoCurrency("2", "Ethereum", "ETH", 3456.78, -1.2),
            CryptoCurrency("3", "Binance Coin", "BNB", 605.20, 0.8),
            CryptoCurrency("4", "Solana", "SOL", 145.50, 5.4),
            CryptoCurrency("5", "Cardano", "ADA", 0.45, -0.5)
        )

        // Phát (emit) dữ liệu ra cho ViewModel thu thập
        emit(mockData)
    }
}