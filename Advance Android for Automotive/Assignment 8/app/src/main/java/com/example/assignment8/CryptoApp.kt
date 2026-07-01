package com.example.assignment8

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//kích hoạt việc tự động tạo code của Hilt
@HiltAndroidApp
class CryptoApp : Application() {
}