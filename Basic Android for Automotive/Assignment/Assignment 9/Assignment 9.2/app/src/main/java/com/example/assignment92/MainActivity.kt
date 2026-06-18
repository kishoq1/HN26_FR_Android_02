package com.example.assignment92

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt

//==============================================
//QUY TRÌNH GỠ LỖI ĐƯỢC ĐÁNH DẤU THEO SỐ THỨ TỰ
//==============================================

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        solveQuadraticEquation()
    }

    private fun solveQuadraticEquation() {
        val a = 2.3
        val b = 4.0
        val c = 5.6
        var root1: Double
        var root2: Double
        val output: String

        val determinant = b * b - 4.0 * a * c

        // condition for real and different roots
        if (determinant > 0) {
            root1 = (-b + sqrt(determinant)) / (2 * a)
            root2 = (b - sqrt(determinant)) / (2 * a)
            output = "root1 = %.2f and root2 = %.2f".format(root1, root2)
        }
        // Condition for real and equal roots
        else if (determinant == 0.0) {
            root2 = -b / (2 * a)
            root1 = root2
            output = "root1 = root2 = %.2f".format(root1)
        }

        //==============================================
        //QUY TRÌNH GỠ LỖI ĐƯỢC ĐÁNH DẤU THEO SỐ THỨ TỰ
        //==============================================

        // If roots are not real
        //5. hệ thống cố gắng tính căn bậc 2 của 1 số âm, điều này là vô lý nên máy tính chỉ có thể báo lỗi NaN
        else {
            val realPart = -b / (2 * a)
            //6. Thêm dấu trừ trước determinant để biến nó thành số dương (code cũ không có dấu trừ)
            val imaginaryPart = sqrt(-determinant) / (2 * a)

            //output = "root1 = %.2f+%.2f+%.2f and root2 = %.2f-%.2f-%.2fi".format(realPart, imaginaryPart, realPart, imaginaryPart)
            // 1. chạy app với code cũ (câu lệnh dòng 54) và thấy app bị crash, mở Logcat và nhập vào ô Filter "FATA EXCEPTION", ta thấy lỗi "java.util.MissingFormatArgumentException: Format specifier '%.2f'"
            // 2. logcat chỉ lỗi ở vị trí Tệp MainActivity.kt:54 tức ở dòng thứ 54 (code cũ), chuỗi String có 6 cái %f nhưng truyền vào hàm .format chỉ có 4 biến
            // 3. sửa lại code đúng
            output = "root1 = %.2f+%.2fi and root2 = %.2f-%.2fi".format(realPart, imaginaryPart, realPart, imaginaryPart)
        }

        Log.d("DebugResult", output)
        //4. Chạy app và tiếp tục xem logcat với Filter:"DebugResult", kết quả là: root1 = -0.87+NaNi and root2 = -0.87-NaNi, lý do là determinant là số âm
        //7. Chạy lại app và xem logcat với Filter:DebugResult", ta thấy kết quả là : root1 = -0.87+1.30i and root2 = -0.87-1.30i (đã chính xác)
    }
}