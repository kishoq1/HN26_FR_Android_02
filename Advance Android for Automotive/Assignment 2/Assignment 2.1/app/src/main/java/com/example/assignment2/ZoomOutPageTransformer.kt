package com.example.assignment2

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs
import kotlin.math.max

class ZoomOutPageTransformer : ViewPager2.PageTransformer {
    private val MIN_SCALE = 0.85f
    private val MIN_ALPHA = 0.5f

    override fun transformPage(view: View, position: Float) {
        view.apply {
            val pageWidth = width
            val pageHeight = height
            when {
                position < -1 -> { // Trang đã bị vuốt khuất hẳn sang trái
                    alpha = 0f
                }
                position <= 1 -> { // Trang đang hiển thị trên màn hình
                    val scaleFactor = max(MIN_SCALE, 1 - abs(position))
                    val vertMargin = pageHeight * (1 - scaleFactor) / 2
                    val horzMargin = pageWidth * (1 - scaleFactor) / 2
                    translationX = if (position < 0) {
                        horzMargin - vertMargin / 2
                    } else {
                        horzMargin + vertMargin / 2
                    }

                    // Thu nhỏ trang (Scale)
                    scaleX = scaleFactor
                    scaleY = scaleFactor

                    // Làm mờ trang (Alpha)
                    alpha = (MIN_ALPHA +
                            (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                }
                else -> { // Trang đã bị vuốt khuất hẳn sang phải
                    alpha = 0f
                }
            }
        }
    }
}