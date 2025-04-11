package com.example.cryptic.presentation.main

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen(navController: NavController) {
    GradientBackgroundHome() {
        Column(modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
            .background(Color(0xFF202126))) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize(0.1f)
            )
            {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.25f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Icon(
                        Icons.Default.Menu, contentDescription = "Меню", modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                            .clickable(true) {
                                // что-то будет
                            },
                        tint = Color.Gray
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                )
                {
                    Text(
                        text = "чаты", modifier = Modifier, style = TextStyle(fontSize = 30.sp),
                        color = Color.White
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize(0.95f),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = {

                    }) {
                        Icon(
                            Icons.Default.Search, contentDescription = "Поиск", modifier = Modifier
                                .fillMaxSize(),
                            tint = Color.Gray
                        )
                    }
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(count = 100) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .fillMaxHeight(0.2f)
                            .background(Color(0xFF202126))
                    ) {
                        Text(text = "hello")
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

