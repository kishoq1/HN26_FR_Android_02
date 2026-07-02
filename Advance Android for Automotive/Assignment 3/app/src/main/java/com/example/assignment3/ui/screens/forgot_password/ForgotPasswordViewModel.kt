package com.example.assignment3.ui.screens.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment3.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    // Bước 1: Gửi OTP (Trong thực tế sẽ gọi API, ở bài tập này ta chỉ chuyển màn hình)
    fun sendOtp(email: String) {
        if (email.isNotBlank()) {
        }
    }

    // Bước 2: Xác nhận OTP
    fun verifyOtp(otp: String) {
        if (otp.length == 6) {
        }
    }

    // Bước 3: Cập nhật mật khẩu mới vào Room Database
    fun updatePassword(email: String, newPassword: String) {
        if (email.isNotBlank() && newPassword.isNotBlank()) {
            viewModelScope.launch {
                repository.updatePassword(email, newPassword)
            }
        }
    }
}