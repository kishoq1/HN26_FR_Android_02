package com.example.assignment3.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.assignment3.R
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun SplashScreen(
    onNavigateToNext: () -> Unit
) {
    LaunchedEffect(key1 = true) {
        delay(2000L.milliseconds)
        onNavigateToNext()
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_big_facebook_logo),
                contentDescription = "Facebook Logo",
                modifier = Modifier.size(120.dp)
            )

            // Phần logo Meta ở dưới cùng
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "from",
                    color = androidx.compose.ui.graphics.Color.Gray,
                    fontSize = 14.sp
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_meta),
                    contentDescription = "Meta Logo",
                    modifier = Modifier
                        .width(80.dp)
                        .height(28.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    // Gọi hàm chính và truyền một khối lệnh rỗng {} vào tham số
    SplashScreen(
        onNavigateToNext = {}
    )
}