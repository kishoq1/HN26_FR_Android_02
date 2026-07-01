package com.example.assignment8.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment8.data.model.CryptoCurrency
import com.example.assignment8.data.repository.CryptoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

// Lớp đại diện cho các trạng thái của màn hình
sealed class CryptoUiState {
    object Loading : CryptoUiState()
    data class Success(val data: List<CryptoCurrency>) : CryptoUiState()
    data class Error(val message: String) : CryptoUiState()
}

//đánh dấu ViewModel này sẽ được Hilt quản lý
@HiltViewModel
//khai báo cho Hilt cách tạo ra class này, đồng thời yêu cầu Hilt tự bom CryptoRepository vào constructor
class CryptoViewModel @Inject constructor(
    private val repository: CryptoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CryptoUiState>(CryptoUiState.Loading)
    val uiState: StateFlow<CryptoUiState> = _uiState.asStateFlow()

    init {
        fetchCryptos()
    }

    private fun fetchCryptos() {
        viewModelScope.launch {
            repository.getCryptoList()
                .onStart {
                    _uiState.value = CryptoUiState.Loading
                }
                .catch { exception ->
                    _uiState.value = CryptoUiState.Error(exception.message ?: "Đã xảy ra lỗi không xác định")
                }
                .collect { cryptoList ->
                    _uiState.value = CryptoUiState.Success(cryptoList)
                }
        }
    }
}