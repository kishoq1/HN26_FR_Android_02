package com.example.assignment3.ui.screens.forgot_password

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindAccountScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToVerifyOtp: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val facebookBlue = Color(0xFF1877F2)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Find Your Account", fontSize = 18.sp, fontWeight = FontWeight.Medium) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Enter your phone number or email address to find your account.",
                textAlign = TextAlign.Center,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            TextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null // Ẩn lỗi khi người dùng bắt đầu gõ lại
                },
                isError = errorMessage != null, // Đổi màu viền nếu có lỗi
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Phone or Email", color = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = facebookBlue,
                    unfocusedIndicatorColor = Color.LightGray,
                    errorIndicatorColor = Color.Red // Màu đỏ khi báo lỗi
                )
            )

            // Hiển thị dòng text báo lỗi nếu errorMessage khác null
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    textAlign = TextAlign.Start
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Logic Validate
                    val isPhone = email.all { it.isDigit() } && email.length == 10
                    val isEmail = email.trim().endsWith("@gmail.com", ignoreCase = true) && email.length > 10

                    if (isPhone || isEmail) {
                        errorMessage = null
                        viewModel.sendOtp(email)
                        onNavigateToVerifyOtp(email)
                    } else {
                        errorMessage = "Please enter a valid 10-digit phone number or @gmail.com email."
                    }
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = facebookBlue),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Continue", color = Color.White)
            }
        }
    }
}