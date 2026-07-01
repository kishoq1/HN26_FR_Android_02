package com.example.assignment2

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tvIndicator: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager2)
        tvIndicator = findViewById(R.id.tvIndicator)

        // 1. Thay thế bằng các link .jpg trực tiếp ổn định và nhẹ hơn
        val sampleImages = listOf(
            "https://picsum.photos/id/1018/800/1000.jpg",
            "https://picsum.photos/id/1015/800/1000.jpg",
            "https://picsum.photos/id/1019/800/1000.jpg",
            "https://picsum.photos/id/1016/800/1000.jpg",
            "https://picsum.photos/id/1025/800/1000.jpg"
        )

        val adapter = ImageAdapter(sampleImages)
        viewPager.adapter = adapter

        // 2. Gắn hiệu ứng chuyển động vừa tạo vào ViewPager2
        viewPager.setPageTransformer(ZoomOutPageTransformer())

        updateIndicator(1, sampleImages.size)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateIndicator(position + 1, sampleImages.size)
            }
        })
    }

    private fun updateIndicator(currentPage: Int, totalPages: Int) {
        tvIndicator.text = "Image $currentPage/$totalPages"
    }
}