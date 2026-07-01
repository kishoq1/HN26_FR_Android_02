package com.example.assignment3.ui.screens.login



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment3.data.local.UserEntity
import com.example.assignment3.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Sealed class quản lý trạng thái an toàn
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: UserEntity) : LoginState()
    data class Error(val message: String) : LoginState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, passcode: String) {
        if (email.isBlank() || passcode.isBlank()) {
            _loginState.value = LoginState.Error("Vui lòng nhập đầy đủ thông tin")
            return
        }

        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            val user = repository.login(email, passcode)
            if (user != null) {
                _loginState.value = LoginState.Success(user)
            } else {
                _loginState.value = LoginState.Error("Email hoặc mật khẩu không chính xác")
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}