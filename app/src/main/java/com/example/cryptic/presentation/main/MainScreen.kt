package com.example.cryptic.presentation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cryptic.R
import com.example.cryptic.presentation.login.User

@Composable
fun HomeScreen(navController: NavController) {
    var showMenu = remember { mutableStateOf(false) }
    val User_test = User()

    GradientBackgroundHome() {
        Column(
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxSize()
                .background(Color(0xFF202126))
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        if (dragAmount > 30 && change.position.x < 50) {
                            showMenu.value = true
                        }
                    }
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)

            ) {
                IconButton(
                    onClick = {
                        showMenu.value = true
                    },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Меню",
                        tint = Color.Gray,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Text(
                    text = "чаты",
                    style = TextStyle(fontSize = 28.sp),
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )

                IconButton(
                    onClick = { },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Поиск",
                        tint = Color.Gray,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(100) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xB7363640)),

                    ) {
                        Text(
                            text = "Чат $it",
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(1.5.dp))
                }
            }
        }
        if (showMenu.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable { showMenu.value = false }
            )
        }

        AnimatedVisibility(
            visible = showMenu.value,
            enter = slideInHorizontally(initialOffsetX = { -it }),
            exit = slideOutHorizontally(targetOffsetX = { -it })
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.8f)
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
                            style = TextStyle(fontSize = 22.sp),
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

                    DrawerItem(
                        "Настройки", Icons.Default.Settings,
                        onClick = {

                        }

                    )

                    Spacer(modifier = Modifier.height(1.dp))

                    DrawerItem(
                        "Профиль", Icons.Default.AccountCircle,
                        onClick = {

                        }

                    )

                    Spacer(modifier = Modifier.height(1.dp))

                    Spacer(modifier = Modifier.weight(1f))

                    DrawerItem(
                        text = "Выйти",
                        icon = Icons.Default.ExitToApp,
                        textColor = Color(0xFFB42424),
                        iconTint = Color.Gray,
                        onClick = {

                        }
                    )

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
        }
    }
}



@Preview(showBackground = true)
@Composable
fun HomeScre() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}

@Composable
fun DrawerItem(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    textColor: Color = Color.White,
    iconTint: Color = Color.Gray
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2F2F36))
            .padding(10.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = TextStyle(fontSize = 22.sp),
            color = textColor
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = iconTint,
            modifier = Modifier
                .size(36.dp)
                .offset(x = (-15).dp)
        )
    }
}