package com.example.assignment4.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment4.databinding.ActivityMainBinding
import com.example.assignment4.ui.screens.AddOccupationActivity
import com.example.assignment4.ui.screens.ClientListActivity
import com.example.assignment4.ui.screens.OccupationListActivity
import com.example.assignment4.ui.screens.ReportActivity
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Nút 1: Mở Form thêm đơn đặt phòng (Task 1)
        binding.btnOpenAddForm.setOnClickListener {
            val intent = Intent(this, AddOccupationActivity::class.java)
            startActivity(intent)
        }

        //Nút 2: Hiển thị danh sách khách hàng
        binding.btnOpenClients.setOnClickListener {
            val intent = Intent(this, ClientListActivity::class.java)
            startActivity(intent)
        }

        // Nút 3: Mở Danh sách đặt phòng (Task 3)
        binding.btnOpenList.setOnClickListener {
            val intent = Intent(this, OccupationListActivity::class.java)
            startActivity(intent)
        }

        // Nút 4: Mở Báo cáo doanh thu (Task 4 & 5)
        binding.btnOpenReports.setOnClickListener {
            val intent = Intent(this, ReportActivity::class.java)
            startActivity(intent)
        }
    }
}