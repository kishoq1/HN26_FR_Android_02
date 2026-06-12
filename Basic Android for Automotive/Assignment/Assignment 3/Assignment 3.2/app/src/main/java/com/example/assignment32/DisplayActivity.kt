package com.example.assignment32

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DisplayActivity : AppCompatActivity() {
    private lateinit var tvDisplay: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_activity)

        tvDisplay = findViewById(R.id.tvDisplay)

        val receivedList = intent.getStringArrayListExtra("GREETINGS_LIST")

        if (receivedList != null && receivedList.isNotEmpty()) {
            tvDisplay.text = receivedList.joinToString(separator = "\n")
        } else {
            tvDisplay.text = "Không nhận được danh sách nào."
        }
    }
}