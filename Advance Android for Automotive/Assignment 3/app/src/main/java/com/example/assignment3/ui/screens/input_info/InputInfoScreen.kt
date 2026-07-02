package com.example.assignment3.ui.screens.input_info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.assignment3.R

@Composable
fun InputInfoScreen(
    viewModel: InputInfoViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToFindAccount: () -> Unit
) {
    val userName by viewModel.savedUserName.collectAsState()

    InputInfoContent(
        userName = userName,
        onNavigateToLogin = onNavigateToLogin,
        onNavigateToFindAccount = onNavigateToFindAccount
    )
}

@Composable
fun InputInfoContent(
    userName: String,
    onNavigateToLogin: () -> Unit,
    onNavigateToFindAccount: () -> Unit
) {
    val facebookBlue = Color(0xFF1877F2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(150.dp))

        // Logo Facebook
        Image(
            painter = painterResource(id = R.drawable.ic_facebook_logo),
            contentDescription = "Facebook Logo",
            modifier = Modifier.size(70.dp)
        )

        Spacer(modifier = Modifier.height(70.dp))

        // Card hiển thị tài khoản
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToLogin() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                // Đã cập nhật: Hiển thị ảnh đại diện thay vì Box màu xám
                Image(
                    painter = painterResource(id = R.drawable.img_avt), // Nhớ đảm bảo bạn có file img_avatar.png trong res/drawable
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop // Cắt ảnh vừa khung mà không làm méo tỉ lệ
                )

                // Biểu tượng thông báo màu đỏ (số 7)
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 6.dp, y = (-6).dp)
                        .background(Color.Red, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("7", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = userName.ifEmpty { "Sanjay Shendy" },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More Options",
                tint = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nút: Log Into Another Account -> Chuyển sang LoginScreen
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToLogin() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "Add Account Icon",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text("Log Into Another Account", color = facebookBlue, fontWeight = FontWeight.Medium, fontSize = 16.sp)
        }

        // Nút: Find Your Account -> Chuyển sang FindAccountScreen
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToFindAccount() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_find),
                contentDescription = "Find Account Icon",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text("Find Your Account", color = facebookBlue, fontWeight = FontWeight.Medium, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { /* Xử lý tạo tài khoản mới */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = facebookBlue),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text("Create New Facebook Account", color = Color.White, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InputInfoScreenPreview() {
    InputInfoContent(
        userName = "Sanjay Shendy",
        onNavigateToLogin = {},
        onNavigateToFindAccount = {}
    )
}