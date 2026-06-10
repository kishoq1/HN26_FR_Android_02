package com.example.assignment1

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etHeight: EditText
    private lateinit var etWeight: EditText
    private lateinit var tvResult: TextView
    private lateinit var btnOk: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etHeight = findViewById(R.id.etHeight)
        etWeight = findViewById(R.id.etWeight)
        tvResult = findViewById(R.id.tvResult)
        btnOk = findViewById(R.id.btnOk)

        btnOk.setOnClickListener {
            calculateAndDisplayBMI()
        }
    }

    @SuppressLint("DefaultLocale")
    private fun calculateAndDisplayBMI() {
        val heightStr = etHeight.text.toString()
        val weightStr = etWeight.text.toString()

        if (heightStr.isNotEmpty() && weightStr.isNotEmpty()) {
            try {
                val height = heightStr.toDouble()
                val weight = weightStr.toDouble()

                if (height > 0) {
                    val bmi = weight / (height * height)

                    tvResult.text = String.format("Result: %.2f", bmi)
                } else {
                    Toast.makeText(this, "Chiều cao phải lớn hơn 0", Toast.LENGTH_SHORT).show()
                }
            } catch (_: NumberFormatException) {
                Toast.makeText(this, "Vui lòng nhập định dạng số hợp lệ", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Vui lòng nhập đầy đủ Chiều cao và Cân nặng", Toast.LENGTH_SHORT).show()
        }
    }
}