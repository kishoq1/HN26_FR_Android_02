package com.example.exam.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exam.ContactApplication
import com.example.exam.databinding.ActivityMainBinding
import com.example.exam.ui.ContactAdapter
import com.example.exam.ui.ContactViewModel
import com.example.exam.ui.ContactViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ContactViewModel
    private lateinit var adapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = (application as ContactApplication).repository
        viewModel = ViewModelProvider(this, ContactViewModelFactory(repository))[ContactViewModel::class.java]

        setupRecyclerView()
        setupListeners()

        lifecycleScope.launch {
            viewModel.contacts.collect { contactsList ->
                adapter.submitList(contactsList)
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = ContactAdapter(
            onItemClick = { contact ->
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("CONTACT_ID", contact.id)
                startActivity(intent)
            },
            onDeleteClick = { contact -> viewModel.deleteContact(contact) }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupListeners() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, EditActivity::class.java))
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { viewModel.updateSearchQuery(s.toString()) }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}