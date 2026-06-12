package com.example.practice1

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton

class ProfileActivity : AppCompatActivity() {
    private lateinit var btnBack : ImageButton
    private lateinit var tvGT : TextView
    private lateinit var tvPlaying : TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)

        btnBack = findViewById<ImageButton>(R.id.btnBack)
        tvGT = findViewById<TextView>(R.id.tvGT)
        tvPlaying = findViewById<TextView>(R.id.tvPlaying)

        val gamerTag = intent.getStringExtra("GAMER_TAG") ?: "Người chơi ẩn danh"
        val selectedGames = intent.getStringArrayListExtra("SELECTED_GAMES")

        tvGT.text = gamerTag
        if (selectedGames != null && selectedGames.isNotEmpty()){
            val formatedGames = selectedGames.joinToString(separator = "\n- ", prefix = "- ")
            tvPlaying.text = "Đang cày:\n$formatedGames"
        }
        else tvPlaying.text = "Chưa chọn tựa game nào."
        btnBack.setOnClickListener { finish() }
    }
}