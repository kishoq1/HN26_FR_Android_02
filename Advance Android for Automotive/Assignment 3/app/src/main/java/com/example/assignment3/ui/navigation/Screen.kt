package com.example.assignment3.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object InputInfo : Screen("input_info_screen")
    object Login : Screen("login_screen")

    object FindAccount : Screen("find_account_screen")

    object VerifyOtp : Screen("verify_otp_screen/{email}") {
        fun createRoute(email: String) = "verify_otp_screen/$email"
    }

    object CreateNewPassword : Screen("create_new_password_screen/{email}") {
        fun createRoute(email: String) = "create_new_password_screen/$email"
    }
}