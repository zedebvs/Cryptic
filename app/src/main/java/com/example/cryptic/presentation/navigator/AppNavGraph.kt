package com.example.cryptic.presentation.navigator

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cryptic.presentation.login.LoginScreen
import com.example.cryptic.presentation.start.StartScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "start") {
        composable("start") {
            StartScreen(navController)
        }
        composable("login") {
            LoginScreen()
        }
        //Надо будет доделать
    }
}