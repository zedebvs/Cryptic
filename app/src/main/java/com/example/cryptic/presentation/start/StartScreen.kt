package com.example.cryptic.presentation.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cryptic.R
import com.example.cryptic.di.LocalTokenManager
import com.example.cryptic.presentation.start.GradientBackground

@Composable
fun StartScreen(navController: NavController) {
    val tokenManager = LocalTokenManager.current

    LaunchedEffect(Unit) {
        if (!tokenManager.getAccessToken().isNullOrEmpty()) {

            navController.navigate("home") {
                popUpTo("start") { inclusive = true }
            }
        }
    }
    GradientBackground() {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize(0.30f)
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = "Логотип",
                    modifier = Modifier
                        .size(164.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Cryptic",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 56.sp),
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(200.dp))

            Button(
                onClick = {
                    navController.navigate("registration")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0F6FDB),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(horizontal = 34.dp, vertical = 18.dp)
                    .height(70.dp),
                shape = RoundedCornerShape(20.dp),

                ) {
                Text(
                    text = "Sign Up",
                    style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account?",
                    style = TextStyle(fontSize = 20.sp),
                    color = Color(0xFFFFFFFF)
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Log In", modifier = Modifier
                        .clickable {
                            navController.navigate("login")
                        },
                    style = TextStyle(fontSize = 28.sp),
                    color = Color(0xFFFFFFFF)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    val navController = rememberNavController()
    StartScreen(navController = navController)
}



