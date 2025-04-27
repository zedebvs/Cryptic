package com.example.cryptic.presentation.main

import android.util.Log
import androidx.compose.animation.EnterTransition.Companion.None
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.cryptic.data.api.models.PublicProfile
import com.example.cryptic.di.LocalSearchViewModel
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.text.font.FontWeight

@Composable
fun UserSearchScreen(navController: NavHostController) {
    val searchQuery = remember { mutableStateOf("") }
    val searchViewModel = LocalSearchViewModel.current
    val searchResults by searchViewModel.searchResults.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            searchViewModel.resetState()
        }
    }

    GradientBackgroundHome {
        Column(
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxSize()
                .background(Color(0xFF1E1E24))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                TextField(
                    value = searchQuery.value,
                    onValueChange = {
                        searchQuery.value = it
                        if (it.isNotBlank()) {
                            searchViewModel.searchUsers(it)
                        } else {
                            searchViewModel.clearSearchResults()
                        }
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color(0xFFA0A0A0))
                    },
                    placeholder = {
                        Text(
                            "Найти пользователя...",
                            color = Color(0xFFA0A0A0)
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        disabledTextColor = Color.White,
                        cursorColor = Color(0xFF6C5CE7),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color(0xFF2A2A32),
                        unfocusedContainerColor = Color(0xFF2A2A32),
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(searchResults) { user ->
                    UserSearchItem(
                        user = user,
                        onViewProfile = { if (user.id != null) {
                            navController.navigate("public_profile/${user.id}")
                        }},
                        onSendMessage = { if (user.id != null) {
                            navController.navigate("chat/${user.id}")
                        } }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun UserSearchItem(
    user: PublicProfile,
    onViewProfile: () -> Unit,
    onSendMessage: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2A2A32)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)) {
                        AsyncImage(
                            model = user.avatar,
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )

                        // Индикатор онлайн статуса
                        if (user.online > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF4CAF50))
                                    .border(2.dp, Color(0xFF2A2A32), CircleShape)
                            )
                        }
                    }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(
                        modifier = Modifier.weight(1f))
                        {
                            Text(
                                text = user.name,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (user.online > 0) "В сети"
                                else formatLastonline(user.lastonline),
                                color = if (user.online > 0) Color(0xFF4CAF50) else Color(0xFFA0A0A0),
                                fontSize = 12.sp
                            )
                        }

                                Row {
                            IconButton(
                                onClick = onViewProfile,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    Icons.Default.AccountCircle,
                                    contentDescription = "Профиль",
                                    tint = Color(0xFF6C5CE7),
                                    modifier = Modifier.size(34.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            IconButton(
                                onClick = onSendMessage,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    Icons.Default.Message,
                                    contentDescription = "Написать",
                                    tint = Color(0xFF6C5CE7),
                                    modifier = Modifier.size(34.dp)
                                )
                            }
                        }
        }
    }
}

fun formatLastonline(lastonline: String?): String {
    if (lastonline.isNullOrEmpty()) return "Нет данных"

    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
        val dateTime = LocalDateTime.parse(lastonline, formatter)

        val now = LocalDateTime.now()
        val today = now.toLocalDate()
        val date = dateTime.toLocalDate()
        val daysAgo = ChronoUnit.DAYS.between(date, today)

        when (daysAgo) {
            0L -> "Был в сети в ${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
            1L -> "Был в сети вчера в ${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
            2L -> "Был в сети позавчера в ${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
            else -> "Был в сети ${dateTime.format(DateTimeFormatter.ofPattern("dd MMMM yyyy 'в' HH:mm", Locale("ru")))}"
        }
    } catch (e: Exception) {
        "Нет данных"
    }
}