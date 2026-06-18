package com.example.assignment91

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val result = binarySearch(23, arrayOf(12, 3, 24, 5, 10, 23, 9))
        Log.d("DebugResult", "Vị trí tìm thấy: $result")
    }

    // Sử dụng Debugger và nhìn vào bảng Threads & Variable để tìm ra lỗi sai
    fun binarySearch(element: Int, array: Array<Int>): Int {
        array.sort() // array = [3, 5, 9, 10, 12, 23, 24]
        var index = 0
        var end = array.size - 1 // end = 6
        while(index <= end) { // <-- Breakpoint đặt ở đây, khi chạy app thì app sẽ bị đóng băng tại câu lệnh này
            val center: Int = (index + end) / 2 // center = 3
            if (element == array[center]){ // element = 23, array[center] = array[3] = 10 != element => nhảy sang đkiện tiếp theo
                return center // bỏ qua
            } else if (element < array[center]){ // element = 23 > array[3] => nhảy sang điều kiện tiếp theo
                end = center - 1 // bỏ qua
            } else if (element > array[center]){ // element 23 > array[3] => thực thi lệnh điều kiện
                // index = center + 2 | index = 5, mà ban đầu index = 3, do đó đã bỏ qua index = 4 => sửa lại code: index = center + 1
                index = center + 1 // code đúng
            }
        }
        return -1
    }
}