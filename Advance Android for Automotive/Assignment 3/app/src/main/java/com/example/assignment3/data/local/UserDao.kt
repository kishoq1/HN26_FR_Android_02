package com.example.assignment3.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    // Kiểm tra đăng nhập
    @Query("SELECT * FROM users WHERE email = :email AND passcode = :passcode LIMIT 1")
    suspend fun login(email: String, passcode: String): UserEntity?

    // Thêm người dùng mới (để tạo fake data)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    // Cập nhật mật khẩu mới khi người dùng quên mật khẩu
    @Query("UPDATE users SET passcode = :newPassword WHERE email = :email")
    suspend fun updatePassword(email: String, newPassword: String)
}