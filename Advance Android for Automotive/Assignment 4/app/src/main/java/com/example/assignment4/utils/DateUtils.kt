package com.example.assignment4.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtils {

    // 1. Chuyển đổi một chuỗi ngày
    fun convertStringToLong(dateString: String, format: String = "dd/MM/yyyy"): Long {
        return try {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            val date = sdf.parse(dateString)
            date?.time ?: 0L
        } catch (e: Exception) {
            e.printStackTrace()
            0L
        }
    }

    // 2. Lấy thời điểm BẮT ĐẦU của một năm
    fun getStartOfYearTimestamp(year: Int): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, Calendar.JANUARY)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    // 3. Lấy thời điểm KẾT THÚC của một năm
    fun getEndOfYearTimestamp(year: Int): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, Calendar.DECEMBER) // Tháng 12
            set(Calendar.DAY_OF_MONTH, 31)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return calendar.timeInMillis
    }
}