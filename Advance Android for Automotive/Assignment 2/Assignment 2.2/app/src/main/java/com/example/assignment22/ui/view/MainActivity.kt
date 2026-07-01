package com.example.assignment22.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment22.databinding.ActivityMainBinding
import com.example.assignment22.databinding.DialogFoodDetailBinding
import com.example.assignment22.data.model.CartItem
import com.example.assignment22.data.model.FoodItem
import com.example.assignment22.data.repository.FoodRepository
import com.example.assignment22.ui.adapter.FoodAdapter
import com.example.assignment22.ui.viewmodel.MainViewModel
import com.example.assignment22.ui.viewmodel.MainViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: FoodAdapter

    private val cartActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val updatedCart = result.data?.getSerializableExtra("UPDATED_CART") as? ArrayList<CartItem>
            updatedCart?.let {
                viewModel.syncCartItems(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = FoodRepository()
        val factory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        adapter = FoodAdapter { selectedFood ->
            showFoodDetailDialog(selectedFood)
        }

        binding.rvFoodItems.layoutManager = LinearLayoutManager(this)
        binding.rvFoodItems.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.foodList.collect { foodList ->
                    adapter.submitList(foodList)
                }
            }
        }

        binding.fabCart.setOnClickListener {
            val currentCart = viewModel.cartItems.value
            if (currentCart.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng đang trống!", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, CartActivity::class.java).apply {
                    putExtra("CART_DATA", ArrayList(currentCart))
                }
                cartActivityLauncher.launch(intent)
            }
        }
    }

    private fun showFoodDetailDialog(foodItem: FoodItem) {
        val dialogBinding = DialogFoodDetailBinding.inflate(layoutInflater)

        dialogBinding.tvDetailName.text = foodItem.name
        dialogBinding.tvDetailDescription.text = foodItem.description
        dialogBinding.tvDetailPrice.text = "$${foodItem.price}"

        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnAddToCart.setOnClickListener {
            viewModel.addToCart(foodItem)
            Toast.makeText(this, "Đã thêm ${foodItem.name} vào giỏ", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }
}