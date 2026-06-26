package com.example.testroomdatabase.ui.edit

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.testroomdatabase.R
import com.example.testroomdatabase.data.model.Contact
import com.example.testroomdatabase.data.room.AppDatabase
import com.example.testroomdatabase.data.room.ContactDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditActivity : AppCompatActivity() {

    private lateinit var contactDao: ContactDao
    private var isEditMode = false
    private var contactId = 0 // Room tự tăng ID nên mặc định dùng 0 cho bản ghi mới
    private var avatarBytes: ByteArray? = null

    private lateinit var imgEditAvatar: ImageView
    private lateinit var edtName: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtEmail: EditText

    // Đăng ký bộ chọn ảnh từ bộ sưu tập
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

        // Khởi tạo DAO từ Room Database
        contactDao = AppDatabase.getDatabase(this).contactDao()

        edtName = findViewById(R.id.edtName)
        edtPhone = findViewById(R.id.edtPhone)
        edtEmail = findViewById(R.id.edtEmail)
        imgEditAvatar = findViewById(R.id.imgEditAvatar)

        val btnSave: Button = findViewById(R.id.btnSave)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        // Kiểm tra xem là Thêm mới hay Chỉnh sửa
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

        // Sự kiện click để chọn ảnh đại diện
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

            // Tạo đối tượng Contact mới. Nếu thêm mới thì id = 0, Room sẽ tự động tăng
            val targetId = if (isEditMode) contactId else 0
            val newContact = Contact(id = targetId, name = name, phone = phone, email = email, avatar = avatarBytes)

            // Đẩy tác vụ ghi Database sang luồng IO để không làm kẹt giao diện
            lifecycleScope.launch(Dispatchers.IO) {
                if (isEditMode) {
                    contactDao.updateContact(newContact)
                } else {
                    contactDao.insertContact(newContact)
                }

                // Quay lại luồng chính (Main Thread) để đóng màn hình
                withContext(Dispatchers.Main) {
                    finish()
                }
            }
        }
    }
}