package com.example.exam.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.exam.ContactApplication
import com.example.exam.data.local.Contact
import com.example.exam.databinding.ActivityEditBinding
import com.example.exam.ui.ContactViewModel
import com.example.exam.ui.ContactViewModelFactory
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private lateinit var viewModel: ContactViewModel
    private var currentContact: Contact? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = (application as ContactApplication).repository
        viewModel = ViewModelProvider(this, ContactViewModelFactory(repository))[ContactViewModel::class.java]

        val contactId = intent.getIntExtra("CONTACT_ID", -1)

        if (contactId != -1) {
            lifecycleScope.launch {
                currentContact = viewModel.getContactById(contactId)
                currentContact?.let { contact ->
                    binding.etName.setText(contact.name)
                    binding.etPhone.setText(contact.phoneNumber)
                    binding.etEmail.setText(contact.email)
                }
            }
        }

        binding.btnCancel.setOnClickListener { finish() }

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val phone = binding.etPhone.text.toString()
            val email = binding.etEmail.text.toString()

            if (currentContact == null) {
                viewModel.addContact(name, phone, email)
            } else {
                currentContact?.let { contact ->
                    val updatedContact = contact.copy(name = name, phoneNumber = phone, email = email)
                    viewModel.updateContact(updatedContact)
                }
            }
            finish() // Quay lại màn hình trước
        }
    }
}