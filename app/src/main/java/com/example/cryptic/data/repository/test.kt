package com.example.cryptic.data.repository

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cryptic.R
import com.example.cryptic.presentation.login.User





@Preview(showBackground = true)
@Composable
fun test() {

    val User_test = User()

    Column(modifier = Modifier
        .fillMaxSize()) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.75f)
            .systemBarsPadding()
            .background(Color.DarkGray)
    ) {
        Column(modifier = Modifier
            .fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.15f)
                    .background(Color(0xFF202126))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = User_test.login,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 26.sp),
                    color = Color.White
                )

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(id = R.drawable.test_image),
                    contentDescription = "профиль",
                    modifier = Modifier
                        .offset(-10.dp)
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )
            }

            Row(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f)
                .background(Color(0xFF2F2F36))
                .padding(10.dp)
                .clickable(){

                },
                verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "Настройки",
                    style = TextStyle( fontSize = 28.sp),
                    color = Color.White
                )
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Настройки",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(36.dp)
                        .offset(-15.dp)
                )
            }

            Spacer(modifier = Modifier.height(1.dp))

            Row(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f)
                .background(Color(0xFF2F2F36))
                .padding(10.dp)
                .clickable(){

                },
                verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "Профиль",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 28.sp),
                    color = Color.White,
                )
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Профиль",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(36.dp)
                        .offset(-15.dp)
                )
            }
            Spacer(modifier = Modifier.height(1.dp))

            Row(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.55f)
                .background(Color(0xFF2F2F36))
                .padding(10.dp)
                .clickable(){

                },
                verticalAlignment = Alignment.Bottom){
                Text(
                    text = "Выйти",
                    style = TextStyle( fontSize = 28.sp),
                    color = (Color(0xFFB42424))
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Выход",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(36.dp)
                        .offset(-15.dp)
                )
            }
            Spacer(modifier = Modifier.height(1.dp))
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF2F2F36))
                    .padding(25.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "О приложении",
                        style = TextStyle(fontSize = 18.sp),
                        color = Color.Gray,
                        modifier = Modifier.clickable {

                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "О приложении",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}}

