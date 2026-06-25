package com.example.exam.ui.edit

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.exam.R
import com.example.exam.data.model.Contact
import com.example.exam.data.sqlite.DatabaseHelper

class EditActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var isEditMode = false
    private var contactId = -1
    private var avatarBytes: ByteArray? = null

    private lateinit var imgEditAvatar: ImageView

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imgEditAvatar.setImageURI(it)
            // Chuyển đổi file ảnh thành mảng byte để lưu Database
            avatarBytes = contentResolver.openInputStream(it)?.readBytes()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        dbHelper = DatabaseHelper(this)

        val edtName: EditText = findViewById(R.id.edtName)
        val edtPhone: EditText = findViewById(R.id.edtPhone)
        val edtEmail: EditText = findViewById(R.id.edtEmail)
        imgEditAvatar = findViewById(R.id.imgEditAvatar)

        val btnSave: Button = findViewById(R.id.btnSave)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        val contactToEdit = intent.getSerializableExtra("CONTACT_DATA") as? Contact
        contactToEdit?.let {
            isEditMode = true
            contactId = it.id
            edtName.setText(it.name)
            edtPhone.setText(it.phone)
            edtEmail.setText(it.email)
            avatarBytes = it.avatar

            if (it.avatar != null) {
                val bitmap = BitmapFactory.decodeByteArray(it.avatar, 0, it.avatar!!.size)
                imgEditAvatar.setImageBitmap(bitmap)
            } else {
                imgEditAvatar.setImageResource(R.mipmap.ic_launcher)
            }
        }

        imgEditAvatar.setOnClickListener {
            pickImage.launch("image/*")
        }

        btnCancel.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {
            val name = edtName.text.toString().trim()
            val phone = edtPhone.text.toString().trim()
            val email = edtEmail.text.toString().trim()

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please enter Name and Phone", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newContact = Contact(id = contactId, name = name, phone = phone, email = email, avatar = avatarBytes)

            if (isEditMode) {
                dbHelper.updateContact(newContact)
            } else {
                dbHelper.addContact(newContact)
            }
            finish()
        }
    }
}