package com.example.assignment33

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btnWay1: Button
    private lateinit var btnWay2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnWay1 = findViewById(R.id.btnWay1)
        btnWay2 = findViewById(R.id.btnWay2)

        btnWay1.setOnClickListener {
            val intent = Intent("com.example.assignment332.ACTION_START_ACTIVITY")
            intent.setPackage("com.example.assignment332")
            startActivity(intent)
        }

        btnWay2.setOnClickListener {
            val intent = Intent("com.example.assignment332.ACTION_START_BROADCAST")

            intent.setPackage("com.example.assignment332")
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
            sendBroadcast(intent)
        }
    }
}