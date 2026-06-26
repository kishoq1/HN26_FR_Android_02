package com.example.testroomdatabase.ui.detail

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.example.testroomdatabase.R
import com.example.testroomdatabase.data.model.Contact
import com.example.testroomdatabase.data.room.AppDatabase
import com.example.testroomdatabase.data.room.ContactDao
import com.example.testroomdatabase.ui.edit.EditActivity
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var contactDao: ContactDao
    private var contactId = -1

    private lateinit var tvName: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvEmail: TextView
    private lateinit var imgDetailAvatar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Khởi tạo DAO từ Room Database
        contactDao = AppDatabase.getDatabase(this).contactDao()

        // Lấy ID từ Intent
        val contact = intent.getSerializableExtra("CONTACT_DATA") as? Contact
        contactId = contact?.id ?: -1

        tvName = findViewById(R.id.tvDetailName)
        tvPhone = findViewById(R.id.tvDetailPhone)
        tvEmail = findViewById(R.id.tvDetailEmail)
        imgDetailAvatar = findViewById(R.id.imgDetailAvatar)

        val btnCall: Button = findViewById(R.id.btnCall)
        val btnEdit: Button = findViewById(R.id.btnEdit)

        btnCall.setOnClickListener {
            // Mở Coroutine để đọc dữ liệu
            lifecycleScope.launch {
                val current = contactDao.getContactById(contactId)
                current?.let {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = "tel:${it.phone}".toUri()
                    startActivity(intent)
                }
            }
        }

        btnEdit.setOnClickListener {
            lifecycleScope.launch {
                val current = contactDao.getContactById(contactId)
                current?.let {
                    val intent = Intent(this@DetailActivity, EditActivity::class.java)
                    intent.putExtra("CONTACT_DATA", it)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Mỗi khi quay lại, tải dữ liệu mới nhất từ DB
        lifecycleScope.launch {
            val updatedContact = contactDao.getContactById(contactId)
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
                // Nếu không tìm thấy (đã bị xóa), đóng Activity
                finish()
            }
        }
    }
}