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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

// 1. MÀN HÌNH CHỨA LOGIC VÀ VIEWMODEL
@Composable
fun CreateNewPasswordScreen(
    email: String,
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    CreateNewPasswordContent(
        onNavigateBack = onNavigateBack,
        onSubmitPassword = { newPassword ->
            // Khi người dùng bấm nút, gọi ViewModel ở đây
            viewModel.updatePassword(email, newPassword)
            onNavigateToLogin()
        }
    )
}

// 2. MÀN HÌNH GIAO DIỆN THUẦN TÚY (Dùng để Preview)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNewPasswordContent(
    onNavigateBack: () -> Unit,
    onSubmitPassword: (String) -> Unit // Truyền password mới ra ngoài qua lambda
) {
    var newPassword by remember { mutableStateOf("") }
    val facebookBlue = Color(0xFF1877F2)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reset your password", fontSize = 18.sp, fontWeight = FontWeight.Medium) },
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
                text = "Create new password",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "You will use this password to access your account. Enter a combination of at least 6 numbers, letters, and punctuation marks.",
                textAlign = TextAlign.Center,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            TextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("New Password", color = Color.Gray) },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = facebookBlue,
                    unfocusedIndicatorColor = Color.LightGray
                )
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    // Trả password mới ra ngoài thay vì gọi ViewModel trực tiếp
                    onSubmitPassword(newPassword)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = facebookBlue),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Log in", color = Color.White)
            }
        }
    }
}

// 3. HÀM PREVIEW GỌI CONTENT STATLESS
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateNewPasswordPreview() {
    CreateNewPasswordContent(
        onNavigateBack = {},
        onSubmitPassword = {} // Hàm rỗng vì ta chỉ cần xem UI
    )
}