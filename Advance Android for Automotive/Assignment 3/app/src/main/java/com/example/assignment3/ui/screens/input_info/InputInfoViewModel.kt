package com.example.assignment3.ui.screens.input_info


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment3.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InputInfoViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    // Trạng thái lưu tên người dùng (Mặc định trống, sẽ cập nhật khi lấy được từ DB)
    private val _savedUserName = MutableStateFlow("Đang tải...")
    val savedUserName: StateFlow<String> = _savedUserName

    init {
        loadSavedUser()
    }

    private fun loadSavedUser() {
        viewModelScope.launch {
            // Giả lập lấy user có email mặc định từ database (Fake data đã tạo ở AppDatabase)
            val user = repository.login("Sanjayshendy123@gmail.com", "12345678")
            if (user != null) {
                _savedUserName.value = user.name
            } else {
                _savedUserName.value = "Sanjay Shendy" // Fallback data
            }
        }
    }
}