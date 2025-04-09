package com.example.cryptic.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen() {
    GradientBackground()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Log In",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 56.sp),
            color = Color.White
        )

        Spacer(modifier = Modifier.height(64.dp))

        var emailText = TextFieldValue("")
        BasicTextField(
            value = emailText,
            onValueChange = { emailText = it },
            textStyle = TextStyle(fontSize = 30.sp),
            modifier = Modifier
                .fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box(
                    Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (emailText.text.isEmpty()) {
                        Text(
                            "Email/Login",
                            color = Color.Gray,
                            modifier = Modifier.wrapContentHeight(Alignment.CenterVertically),
                            style = TextStyle(fontSize = 24.sp)
                        )
                    }
                    innerTextField()
                }
            }
        )
        var passwordText = TextFieldValue("")
        BasicTextField(
            value = passwordText,
            onValueChange = { passwordText = it },
            textStyle = TextStyle(fontSize = 30.sp),
            modifier = Modifier
                .fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box(
                    Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (passwordText.text.isEmpty()) {
                        Text(
                            "Password",
                            color = Color.Gray,
                            modifier = Modifier.wrapContentHeight(Alignment.CenterVertically),
                            style = TextStyle(fontSize = 24.sp)
                        )
                    }
                    innerTextField()
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Forgot password?",
                style = TextStyle(fontSize = 24.sp),
                color = Color.White,
                modifier = Modifier.clickable { },
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {  },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0F71DE),
                contentColor = Color.White),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
                .padding(horizontal = 8.dp, vertical = 24.dp)
                .height(64.dp),
            shape = RoundedCornerShape(20.dp),
        ) {
            Text(
                text = "Log In",
                style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center){
            Text(text = "Don't have an account?",
                style = TextStyle(fontSize = 16.sp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Sign in",
                modifier = Modifier.clickable {
                    
                },
                style = TextStyle(fontSize = 20.sp),
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LoginScreen()
}

@Composable
fun GradientBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0578F3),
                        Color(0xFF5E1787)
                    )
                )
            )
    )
}

@Preview(showBackground = true)
@Composable
fun GradientBackgroundPreview() {
    GradientBackground()
}