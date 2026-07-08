package com.example.assignment10

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import com.example.assignment10.R
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.assignment10.ui.theme.Assignment10Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment10Theme {
                CreateAccountScreen()
            }
        }
    }
}

@Composable
fun CreateAccountScreen() {
    //Trạng thái lưu trữ dữ liệu nhập liệu
    var username by remember { mutableStateOf("Cody Fisher") }
    var email by remember { mutableStateOf("michae.mitc@example.com") }
    var password by remember { mutableStateOf("12345678") }
    var confirmPassword by remember { mutableStateOf("12345678") }
    var agreeToTerms by remember { mutableStateOf(true) }

    //Bảng màu cơ bản của UI
    val backgroundColor = Color(0xFF1E1E24)
    val inputBackgroundColor = Color(0xFF2A2A32)
    val textColor = Color.White
    val subtleTextColor = Color.Gray

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        //Tiêu đề
        Text(
            text = "Create Account",
            color = textColor,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(40.dp))

        //Trường nhập Username
        CustomTextField(
            value = username,
            onValueChange = {username = it},
            label = "Username",
            containerColor = inputBackgroundColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        //Trường nhập email
        CustomTextField(
            value = email,
            onValueChange = {email = it},
            label = "Email",
            containerColor = inputBackgroundColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        //Hàng chứa ô nhập mật khẩu
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CustomTextField(
                value = password,
                onValueChange = {password = it},
                label = "Password",
                isPassword = true,
                containerColor = inputBackgroundColor,
                modifier = Modifier.weight(1f),
            )


            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = confirmPassword,
                onValueChange = {confirmPassword = it},
                label = "Confirm",
                isPassword = true,
                containerColor = inputBackgroundColor,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        //Checkbox điều khoản
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = agreeToTerms,
                onCheckedChange = {agreeToTerms = it},
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFFB145FF),
                    uncheckedColor = subtleTextColor
                )
            )
            Text(
                text = "By creating an account, you agree to the Terms\nof Service and Privacy Policy",
                color = subtleTextColor,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Nút register với dải màu Gradient
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFFB145FF), Color(0xFF4579FF))
                    ),
                    shape = RoundedCornerShape(25.dp)
                ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues()
        ) {
            Text(
                text = "Register",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        //dòng phân cách
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = subtleTextColor.copy(alpha = 0.3f))
            Text(
                text = "or",
                modifier = Modifier.padding(horizontal = 16.dp),
                color = subtleTextColor,
                fontSize = 14.sp
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = subtleTextColor.copy(alpha = 0.3f))
        }

        Spacer(modifier = Modifier.height(32.dp))

        //Các nút đăng nhập mạng xã hội
        SocialLoginButton(text = "   Continue with Google")
        Spacer(modifier = Modifier.height(16.dp))
        SocialLoginButton(text = "   Continue with Facebook")
    }

}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    containerColor: Color,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier



){
    var isPasswordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.Gray)},
        visualTransformation = if (isPassword && !isPasswordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        painter = painterResource(
                            id = if (isPasswordVisible) R.drawable.ic_eye_close else R.drawable.ic_eye
                        ),
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                        tint = Color.Gray
                    )
                }
            }
        } else null,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            focusedBorderColor = Color(0xFF4579FF),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        singleLine = true
    )
}

@Composable
fun SocialLoginButton(text: String){
    OutlinedButton(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(25.dp),
        border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f)),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
    ) {
        if(text.contains("Continue with Google")) Image(painter = painterResource(id = R.drawable.ic_google), contentDescription = "Google Icon", modifier = Modifier.size(24.dp))
        else Image(painter = painterResource(id = R.drawable.ic_facebook), contentDescription = "Facebook icon", modifier = Modifier.size(24.dp))
        Text(text = text, fontWeight = FontWeight.Medium)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateAccountScreenPreview() {
    Assignment10Theme {
        CreateAccountScreen()
    }
}