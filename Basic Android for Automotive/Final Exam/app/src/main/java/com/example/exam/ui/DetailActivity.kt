package com.example.exam.ui



import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.exam.ContactApplication
import com.example.exam.data.local.Contact
import com.example.exam.databinding.ActivityDetailBinding
import com.example.exam.ui.ContactViewModel
import com.example.exam.ui.ContactViewModelFactory
import kotlinx.coroutines.launch
import androidx.core.net.toUri

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: ContactViewModel
    private var currentContact: Contact? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = (application as ContactApplication).repository
        viewModel = ViewModelProvider(this, ContactViewModelFactory(repository))[ContactViewModel::class.java]

        val contactId = intent.getIntExtra("CONTACT_ID", -1)
        if (contactId != -1) {
            lifecycleScope.launch {
                currentContact = viewModel.getContactById(contactId)
                currentContact?.let { contact ->
                    binding.tvName.text = "Name: ${contact.name}"
                    binding.tvPhone.text = "Phone: ${contact.phoneNumber}"
                    binding.tvEmail.text = "Email: ${contact.email}"
                }
            }
        }

        binding.btnCall.setOnClickListener {
            currentContact?.let { contact ->
                val intent = Intent(Intent.ACTION_DIAL, "tel:${contact.phoneNumber}".toUri())
                startActivity(intent)
            }
        }

        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra("CONTACT_ID", contactId)
            startActivity(intent)
            finish() // Tắt màn hình này để khi Edit lưu xong sẽ back về Main (hoặc bạn có thể giữ lại tùy luồng)
        }
    }
}