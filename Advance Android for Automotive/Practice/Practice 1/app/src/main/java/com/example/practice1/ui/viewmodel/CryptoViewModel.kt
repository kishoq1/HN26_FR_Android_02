package com.example.practice1.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice1.data.model.CryptoCurrency
import com.example.practice1.data.repository.ICryptoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class CryptoUiState{
    object Loading : CryptoUiState()
    data class Success(val data: List<CryptoCurrency>) : CryptoUiState()
    data class Error(val message: String) : CryptoUiState()
}

@HiltViewModel
class CryptoViewModel @Inject constructor(
    private val repository : ICryptoRepository
) : ViewModel(){
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    private val _rawUiState = MutableStateFlow<CryptoUiState>(CryptoUiState.Loading)
    val uiState : StateFlow<CryptoUiState> = combine(
        _rawUiState,
        _searchQuery
    ){state, query ->
        when(state){
            is CryptoUiState.Success ->{
                if(query.isBlank()) CryptoUiState.Success(state.data)
                else{
                    val filteredList = state.data.filter { coin -> coin.name.contains(query,ignoreCase = true) || coin.symbol.contains(query, ignoreCase = true) }
                    CryptoUiState.Success(filteredList)
                }
            }
            else -> state
        }
    }
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CryptoUiState.Loading
        )

    init {
        fetchCryptos()
    }

    private fun fetchCryptos() {
        viewModelScope.launch {
            repository.getCryptoList()
                .onStart {
                    _rawUiState.value = CryptoUiState.Loading
                }
                .catch { exception ->
                    _rawUiState.value = CryptoUiState.Error(exception.message ?: "Đã xảy ra lỗi không xác định")
                }
                .collect { cryptoList ->
                    _rawUiState.value = CryptoUiState.Success(cryptoList)
                }
        }
    }

    fun updateSearchQuery(newQuery : String){
        _searchQuery.value = newQuery
    }
}