package com.example.assignment7

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// 1. Định nghĩa các đường dẫn API
interface ApiService {
    @POST("api/sync")
    suspend fun syncTransactions(@Body transactions: List<TransactionEntity>): Response<Void>
}

// 2. Khởi tạo cục bộ Retrofit (Singleton)
object RetrofitClient {
    private const val BASE_URL = "https://fpt-mock-server.com/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}