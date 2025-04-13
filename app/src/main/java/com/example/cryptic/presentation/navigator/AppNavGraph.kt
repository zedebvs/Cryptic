package com.example.cryptic.presentation.navigator

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cryptic.presentation.ResetPassword.ResetPasswordScreen
import com.example.cryptic.presentation.login.LoginScreen
import com.example.cryptic.presentation.main.HomeScreen
import com.example.cryptic.presentation.main.ProfileScreen
import com.example.cryptic.presentation.main.SettingsScreen
import com.example.cryptic.presentation.register.RegisterScreen
import com.example.cryptic.presentation.start.StartScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "start") {
        composable("start") {
            StartScreen(navController)
        }
        composable("login") {
            LoginScreen(navController)
        }
        composable("registration") {
            RegisterScreen(navController)
        }
        composable("reset_password") {
            ResetPasswordScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("profile") {
            ProfileScreen(navController)
        }
        composable("settings") {
            SettingsScreen(navController)
        }
        composable("exit") {
            HomeScreen(navController)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    val navController = rememberNavController()
    AppNavGraph(navController = navController)
}