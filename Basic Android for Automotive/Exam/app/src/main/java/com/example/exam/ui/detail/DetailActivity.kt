package com.example.exam.ui.detail

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.exam.R
import com.example.exam.data.model.Contact
import com.example.exam.data.sqlite.DatabaseHelper
import com.example.exam.ui.edit.EditActivity

class DetailActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var contactId = -1

    private lateinit var tvName: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvEmail: TextView
    private lateinit var imgDetailAvatar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        dbHelper = DatabaseHelper(this)
        val contact = intent.getSerializableExtra("CONTACT_DATA") as? Contact
        contactId = contact?.id ?: -1

        tvName = findViewById(R.id.tvDetailName)
        tvPhone = findViewById(R.id.tvDetailPhone)
        tvEmail = findViewById(R.id.tvDetailEmail)
        imgDetailAvatar = findViewById(R.id.imgDetailAvatar)

        val btnCall: Button = findViewById(R.id.btnCall)
        val btnEdit: Button = findViewById(R.id.btnEdit)

        btnCall.setOnClickListener {
            val current = dbHelper.getContactById(contactId)
            current?.let {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = "tel:${it.phone}".toUri()
                startActivity(intent)
            }
        }

        btnEdit.setOnClickListener {
            val current = dbHelper.getContactById(contactId)
            current?.let {
                val intent = Intent(this, EditActivity::class.java)
                intent.putExtra("CONTACT_DATA", it)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val updatedContact = dbHelper.getContactById(contactId)
        if (updatedContact != null) {
            tvName.text = updatedContact.name
            tvPhone.text = updatedContact.phone
            tvEmail.text = updatedContact.email

            if (updatedContact.avatar != null) {
                val bitmap = BitmapFactory.decodeByteArray(updatedContact.avatar, 0, updatedContact.avatar!!.size)
                imgDetailAvatar.setImageBitmap(bitmap)
            } else {
                imgDetailAvatar.setImageResource(R.mipmap.ic_launcher)
            }
        } else {
            finish()
        }
    }
}