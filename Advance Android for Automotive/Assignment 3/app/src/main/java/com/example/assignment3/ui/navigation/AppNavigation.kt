package com.example.assignment3.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.assignment3.ui.screens.forgot_password.CreateNewPasswordScreen
import com.example.assignment3.ui.screens.forgot_password.FindAccountScreen
import com.example.assignment3.ui.screens.forgot_password.VerifyOtpScreen
import com.example.assignment3.ui.screens.input_info.InputInfoScreen
import com.example.assignment3.ui.screens.login.LoginScreen
import com.example.assignment3.ui.screens.splash.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {

        // --- 1. Màn hình Splash ---
        composable(route = Screen.Splash.route) {
            SplashScreen(
                onNavigateToNext = {
                    // Chuyển sang màn hình InputInfo và xóa Splash khỏi ngăn xếp (Back-stack)
                    navController.navigate(Screen.InputInfo.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // --- 2. Màn hình Profile Login (Input Info) ---
        composable(route = Screen.InputInfo.route) {
            InputInfoScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        // --- 3. Màn hình Login ---
        composable(route = Screen.Login.route) {
            LoginScreen(
                onNavigateToForgotPassword = {
                    // Chuyển hướng sang bước 1 của luồng Quên mật khẩu
                    navController.navigate(Screen.FindAccount.route)
                },
                onLoginSuccess = {
                    // (Tùy chọn) Xử lý logic khi đăng nhập thành công
                    // Ví dụ: navController.navigate(Screen.Home.route) { popUpTo(Screen.Login.route) { inclusive = true } }
                }
            )
        }

        // --- 4. Tìm Tài Khoản (Luồng Quên mật khẩu B1) ---
        composable(route = Screen.FindAccount.route) {
            FindAccountScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToVerifyOtp = { email ->
                    // Truyền tham số email vừa nhập sang màn hình VerifyOtp
                    navController.navigate(Screen.VerifyOtp.createRoute(email))
                }
            )
        }

        // --- 5. Nhập mã OTP (Luồng Quên mật khẩu B2) ---
        composable(
            route = Screen.VerifyOtp.route,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            // Trích xuất chuỗi email từ Route
            val email = backStackEntry.arguments?.getString("email") ?: ""

            VerifyOtpScreen(
                email = email,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCreatePassword = {
                    // Tiếp tục truyền email sang màn hình CreateNewPassword
                    navController.navigate(Screen.CreateNewPassword.createRoute(email))
                }
            )
        }

        // --- 6. Tạo Mật Khẩu Mới (Luồng Quên mật khẩu B3) ---
        composable(
            route = Screen.CreateNewPassword.route,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            // Trích xuất chuỗi email để ViewModel sử dụng cho việc cập nhật DB
            val email = backStackEntry.arguments?.getString("email") ?: ""

            CreateNewPasswordScreen(
                email = email,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLogin = {
                    // Sau khi đổi mật khẩu thành công, điều hướng thẳng về màn hình Login
                    // popUpTo inclusive = true giúp xóa toàn bộ 3 màn hình Quên mật khẩu trước đó
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
    }
}