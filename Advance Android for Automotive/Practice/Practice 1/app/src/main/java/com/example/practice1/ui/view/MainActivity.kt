package com.example.practice1.ui.view

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practice1.R
import com.example.practice1.ui.viewmodel.CryptoUiState
import com.example.practice1.ui.viewmodel.CryptoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue
import androidx.core.widget.addTextChangedListener
import android.widget.EditText

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Hilt sẽ tự động tìm và tiêm CryptoViewModel vào đây
    private val viewModel: CryptoViewModel by viewModels()

    private lateinit var adapter: CryptoAdapter
    private lateinit var rvCrypto: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etSearch = findViewById<EditText>(R.id.etSearch)

        etSearch.addTextChangedListener { text ->
            viewModel.updateSearchQuery(text.toString())
        }

        rvCrypto = findViewById(R.id.rvCrypto)
        progressBar = findViewById(R.id.progressBar)

        adapter = CryptoAdapter(emptyList())
        rvCrypto.layoutManager = LinearLayoutManager(this)
        rvCrypto.adapter = adapter

        // Lắng nghe dữ liệu (StateFlow) từ ViewModel
        observeViewModel()
    }

    private fun observeViewModel() {
        // Sử dụng lifecycleScope để thu thập dữ liệu một cách an toàn
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is CryptoUiState.Loading -> {
                            // Hiển thị vòng xoay, ẩn danh sách
                            progressBar.visibility = View.VISIBLE
                            rvCrypto.visibility = View.GONE
                        }
                        is CryptoUiState.Success -> {
                            // Ẩn vòng xoay, cập nhật dữ liệu lên RecyclerView
                            progressBar.visibility = View.GONE
                            rvCrypto.visibility = View.VISIBLE
                            adapter.updateData(state.data)
                        }
                        is CryptoUiState.Error -> {
                            // Ẩn vòng xoay, thông báo lỗi
                            progressBar.visibility = View.GONE
                            Toast.makeText(this@MainActivity, state.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}