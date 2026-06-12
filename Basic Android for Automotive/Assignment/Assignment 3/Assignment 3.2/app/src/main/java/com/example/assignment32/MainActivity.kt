package com.example.assignment32

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var btnSend: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSend = findViewById(R.id.btnSend)

        val greetingsList = arrayListOf(
            "Hello!", "Hi!", "Salut!", "Hallo!", "Ciao!", "Ahoj!",
            "YAH sahs!", "Bog!", "Hej!", "Czesc!", "Ní hảo!",
            "Kon'nichiwa!", "Annyeonghaseyo!", "Shalom!",
            "Sah-wahd-dee-kah!", "Merhaba!", "Hujambo!", "Olá!"
        )

        btnSend.setOnClickListener {
            // Khởi tạo Intent để chuyển sang DisplayActivity
            val intent = Intent(this, DisplayActivity::class.java)

            intent.putStringArrayListExtra("GREETINGS_LIST", greetingsList)

            startActivity(intent)
        }
    }
}