package com.example.assignment22.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment22.data.model.CartItem
import com.example.assignment22.data.model.FoodItem
import com.example.assignment22.data.repository.FoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(private val repository: FoodRepository) : ViewModel() {

    private val _foodList = MutableStateFlow<List<FoodItem>>(emptyList())
    val foodList: StateFlow<List<FoodItem>> = _foodList.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    init {
        loadFoodItems()
    }

    private fun loadFoodItems() {
        viewModelScope.launch {
            val data = repository.getMockFoodList()
            _foodList.value = data
        }
    }

    fun addToCart(foodItem: FoodItem) {
        _cartItems.update { currentCart ->
            val existingItem = currentCart.find { it.foodItem.id == foodItem.id }

            if (existingItem != null) {
                currentCart.map {
                    if (it.foodItem.id == foodItem.id) it.copy(quantity = it.quantity + 1)
                    else it
                }
            } else {
                currentCart + CartItem(foodItem, 1)
            }
        }
    }

    fun removeFromCart(foodItem: FoodItem) {
        _cartItems.update { currentCart ->
            val existingItem = currentCart.find { it.foodItem.id == foodItem.id } ?: return@update currentCart

            if (existingItem.quantity > 1) {
                currentCart.map {
                    if (it.foodItem.id == foodItem.id) it.copy(quantity = it.quantity - 1)
                    else it
                }
            } else {
                currentCart.filter { it.foodItem.id != foodItem.id }
            }
        }
    }

    fun getTotalPrice(): Double {
        return _cartItems.value.sumOf { it.foodItem.price * it.quantity }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun syncCartItems(updatedCart: List<CartItem>) {
        _cartItems.value = updatedCart
    }
}