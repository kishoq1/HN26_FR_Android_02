package com.example.assignment11

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme{
                ProfileScreen()
            }
        }
    }
}

//Màn hình chính
@Composable
fun ProfileScreen() {
    val backgroundColor = Color(0xFFF3F4F6)

    Scaffold(
        bottomBar = {AppBottomNavigation()},
        containerColor = backgroundColor,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            //Tiêu đề
            Text(
                text = "Profile",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(32.dp))

            //Avatar
            ProfileAvatar("https://images.prod.boo.dating/database/profiles/16986524252684ea75002cf07dd377f4409b1702c68ac.jpeg")

            Spacer(modifier = Modifier.height(32.dp))

            //Card thông tin cá nhân
            SectionCard("Personal info"){
                InfoItemRow(
                    icon = Icons.Outlined.Person,
                    label = "Name",
                    value = "Ushijima Wakatoshi"
                )

                InfoItemRow(
                    icon = Icons.Outlined.Email,
                    label = "Email",
                    value = "ushiwaka@gmail.com"
                )

                InfoItemRow(
                    icon = Icons.Outlined.Phone,
                    label = "Phone number",
                    value = "+63636363663"
                )

                InfoItemRow(
                    icon = Icons.Outlined.Home,
                    label = "Home address",
                    value = "Miyagi, Japan"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SectionCard("Account info") {
                Spacer(modifier = Modifier.height(40.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

//Composable cho Avatar
@Composable
fun ProfileAvatar(imgUrl : String){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(110.dp)
    ){
        //Load ảnh từ internet bằng coil
        AsyncImage(
            model = imgUrl,
            contentDescription = "User Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        )

        //Nút Edit nổi
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(32.dp)
                .clip(CircleShape)
                .background(Color.White)
                .padding(4.dp),
            contentAlignment = Alignment.Center,
        ){
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = "Edit Avatar",
                modifier = Modifier.size(18.dp),
                tint = Color.Black
            )
        }
    }
}

//Compose tái sử dụng cho các card
@Composable
fun SectionCard(title: String, content: @Composable () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                ambientColor = Color.Gray,
                spotColor = Color.Black
            ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = "Edit",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

//Compose tái sử dụng cho từng thông tin
@Composable
fun InfoItemRow(icon : ImageVector, label: String, value: String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = label,
                color = Color.Gray,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun AppBottomNavigation(){
    NavigationBar(
        modifier = Modifier
            .clip(RoundedCornerShape(
                topEnd = 20.dp,
                topStart = 20.dp)),
        containerColor = Color.White,
        contentColor = Color.Gray,
        tonalElevation = 0.dp,
    ) {
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = {Icon(Icons.Outlined.Home, contentDescription = "Home")},
            label = {Text("Home", fontSize = 10.sp)},
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray)
        )

        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = {Icon(Icons.Outlined.Map, contentDescription = "Map")},
            label = {Text("Map", fontSize = 10.sp )},
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray)
        )

        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = {Icon(Icons.Outlined.SwapHoriz, contentDescription = "Transfer")},
            label = {Text("Transfer", fontSize = 10.sp)},
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray)
        )

        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = {Icon(Icons.Outlined.Settings, contentDescription = "Setting")},
            label = {Text("Setting", fontSize = 10.sp)},
            colors = NavigationBarItemDefaults.colors(unselectedTextColor = Color.Gray, unselectedIconColor = Color.Gray)
        )

        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = {AsyncImage(
                model = "https://images.prod.boo.dating/database/profiles/16986524252684ea75002cf07dd377f4409b1702c68ac.jpeg",
                contentDescription = "User Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )},
            label = {Text("Profile", fontSize = 10.sp)},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, selectedTextColor = Color.Black, indicatorColor = Color.Transparent)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview(){
    MaterialTheme {
        ProfileScreen()
    }
}
