package com.example.assignment2

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvAge: TextView
    private lateinit var tvMajor: TextView
    private lateinit var tvFavourite: TextView

    private val getResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val returnedValue = data?.getStringExtra("RETURNED_VALUE")
            val fieldType = data?.getStringExtra("FIELD_TYPE")

            when (fieldType) {
                "name" -> tvName.text = returnedValue
                "age" -> tvAge.text = returnedValue
                "major" -> tvMajor.text = returnedValue
                "favourite" -> tvFavourite.text = returnedValue
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvName = findViewById(R.id.tvName)
        tvAge = findViewById(R.id.tvAge)
        tvMajor = findViewById(R.id.tvMajor)
        tvFavourite = findViewById(R.id.tvFavourite)

        tvName.setOnClickListener { openInputActivity("name") }
        tvAge.setOnClickListener { openInputActivity("age") }
        tvMajor.setOnClickListener { openInputActivity("major") }
        tvFavourite.setOnClickListener { openInputActivity("favourite") }
    }

    private fun openInputActivity(fieldType: String) {
        val intent = Intent(this, InputValueActivity::class.java)
        intent.putExtra("FIELD_TYPE", fieldType)

        getResult.launch(intent)
    }
}