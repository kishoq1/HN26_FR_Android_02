package com.example.assignment8.di

import com.example.assignment8.data.repository.CryptoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//đánh dấu lớp AppModule là bản vẽ hướng dẫn cho Hilt
@Module
//Xác định phạm vi sống của module, SingletonComponent nghĩa là sống cùng vòng đời ứng dụng
@InstallIn(SingletonComponent::class)
object AppModule {

    // báo cho Hilt biết hàm này là nơi tạo ra và trả về đối tượng cần thiết
    @Provides
    // Đảm bảo Hilt chỉ tạo ra duy nhất 1 bản thể
    @Singleton
    fun provideCryptoRepository(): CryptoRepository {
        return CryptoRepository()
    }
}