package com.example.cryptic.presentation.ResetPassword


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cryptic.presentation.start.GradientBackground

@Composable
fun ResetPasswordScreen(navController: NavController) {
    var step = remember { mutableStateOf(1) }

    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 120.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Reset Password",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(40.dp))

            when (step.value) {
                1 -> EnterEmailBlock(onNext = { step.value = 2 })
                2 -> EnterCodeBlock(onNext = { step.value = 3 })
                3 -> EnterNewPasswordBlock(onFinish = {
                    navController.navigate("login") {
                        popUpTo("start") { inclusive = false }
                        launchSingleTop = true
                    }
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    val navController = rememberNavController()
    ResetPasswordScreen(navController = navController)
}

@Composable
fun EnterEmailBlock(onNext: () -> Unit) {
    var email by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Enter your email address", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                value = email,
                onValueChange = {

                },
                label = {
                    Text(
                        text = "Email"
                    )
                        },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onNext() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Send")
            }
        }
    }
}
@Composable
fun EnterCodeBlock(onNext: () -> Unit) {
    var code by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("A code was sent to your email. Enter it below", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                value = code,
                onValueChange = { code = it },
                label = { Text("Verification Code") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onNext() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Verify")
            }
        }
    }
}
@Composable
fun EnterNewPasswordBlock(onFinish: () -> Unit) {
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Enter your new password", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                value = password,
                onValueChange = {

                },
                label = { Text("New Password") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                value = repeatPassword,
                onValueChange = {

                },
                label = { Text("Repeat Password") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                        onFinish()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Change Password")
            }
        }
    }
}