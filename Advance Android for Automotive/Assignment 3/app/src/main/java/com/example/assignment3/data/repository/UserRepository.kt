package com.example.assignment3.data.repository

import com.example.assignment3.data.local.UserDao
import com.example.assignment3.data.local.UserEntity


class UserRepository(private val userDao: UserDao) {

    // Gọi hàm login từ DAO
    suspend fun login(email: String, passcode: String): UserEntity? {
        return userDao.login(email, passcode)
    }

    // Gọi hàm cập nhật mật khẩu từ DAO
    suspend fun updatePassword(email: String, newPassword: String) {
        userDao.updatePassword(email, newPassword)
    }
}