package com.example.cryptic.presentation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.cryptic.di.LocalPublicProfileViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay

@Composable
fun PublicProfileScreen(navController: NavHostController, userId: Int) {
    val publicProfileViewModel = LocalPublicProfileViewModel.current
    val userProfileState by publicProfileViewModel.profile.collectAsState()
    val user = userProfileState

    val animatedVisible = remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        publicProfileViewModel.loadUserProfile(userId)
        delay(100)
        animatedVisible.value = true
    }

    GradientBackgroundHome {
        if (user == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .systemBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(visible = animatedVisible.value) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Spacer(modifier = Modifier.height(16.dp))

                        AsyncImage(
                            model = user.avatar,
                            contentDescription = "Аватар",
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape)
                                .background(Color.Gray)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = user.name,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (user.online > 0) "В сети" else formatLastonline(user.lastonline),
                            fontSize = 16.sp,
                            color = if (user.online > 0) Color(0xFF4CAF50) else Color.Gray
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF2F2F36)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = user.status ?: "Нет статуса",
                                fontSize = 18.sp,
                                color = Color.LightGray,
                                modifier = Modifier
                                    .padding(vertical = 12.dp, horizontal = 8.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = {
                                navController.navigate("chat/${user.id}")
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(text = "Написать сообщение", fontSize = 18.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}