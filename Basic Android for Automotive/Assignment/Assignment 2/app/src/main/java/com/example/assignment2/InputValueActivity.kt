package com.example.assignment2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InputValueActivity : AppCompatActivity() {

    private var currentFieldType: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_value)

        val tvInputTitle = findViewById<TextView>(R.id.tvInputTitle)
        val etInputInfo = findViewById<EditText>(R.id.etInputInfo)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        currentFieldType = intent.getStringExtra("FIELD_TYPE")
        tvInputTitle.text = "Input $currentFieldType"
        when (currentFieldType) {
            "age" -> {
                etInputInfo.inputType = InputType.TYPE_CLASS_NUMBER
                etInputInfo.hint = "Example: 23"
            }
            "name" -> {
                etInputInfo.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
            }
            else -> {
                etInputInfo.inputType = InputType.TYPE_CLASS_TEXT
            }
        }
        etInputInfo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s.toString()
                var isValid = false

                when (currentFieldType) {
                    "age" -> {
                        val ageValue = input.toIntOrNull()
                        isValid = ageValue != null && ageValue in 1..120
                    }
                    "name" -> {
                        isValid = input.trim().length >= 2
                    }
                    else -> {
                        isValid = input.trim().isNotEmpty()
                    }
                }

                btnSubmit.isEnabled = isValid
                btnSubmit.alpha = if (isValid) 1.0f else 0.5f
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnSubmit.setOnClickListener {
            val enteredText = etInputInfo.text.toString()

            val resultIntent = Intent()
            resultIntent.putExtra("RETURNED_VALUE", enteredText)
            resultIntent.putExtra("FIELD_TYPE", currentFieldType)

            setResult(RESULT_OK, resultIntent)
            finish()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}