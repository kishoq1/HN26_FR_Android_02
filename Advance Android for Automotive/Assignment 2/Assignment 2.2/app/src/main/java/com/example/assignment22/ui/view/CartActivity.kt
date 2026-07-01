package com.example.assignment22.ui.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment22.data.model.CartItem
import com.example.assignment22.databinding.ActivityCartBinding
import com.example.assignment22.ui.adapter.CartAdapter

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: CartAdapter
    private var cartList = arrayListOf<CartItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartList = intent.getSerializableExtra("CART_DATA") as? ArrayList<CartItem> ?: arrayListOf()

        adapter = CartAdapter { itemToRemove ->
            removeCartItem(itemToRemove)
        }

        binding.rvCartItems.layoutManager = LinearLayoutManager(this)
        binding.rvCartItems.adapter = adapter
        adapter.submitList(cartList.toList())

        updateTotalPrice()

        binding.btnConfirm.setOnClickListener {
            if (cartList.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng đang trống!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show()

            cartList.clear()

            finishWithResult()
        }
    }

    private fun removeCartItem(itemToRemove: CartItem) {
        val index = cartList.indexOfFirst { it.foodItem.id == itemToRemove.foodItem.id }
        if (index != -1) {
            val currentItem = cartList[index]
            if (currentItem.quantity > 1) {
                cartList[index] = currentItem.copy(quantity = currentItem.quantity - 1)
            } else {
                cartList.removeAt(index)
            }

            adapter.submitList(cartList.toList())

            updateTotalPrice()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateTotalPrice() {
        val total = cartList.sumOf { it.foodItem.price * it.quantity }
        binding.tvTotalPrice.text = "Tổng tiền: $${"%.2f".format(total)}"
    }

    override fun onBackPressed() {
        finishWithResult()
        super.onBackPressed()
    }

    private fun finishWithResult() {
        val resultIntent = Intent().apply {
            putExtra("UPDATED_CART", cartList)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}