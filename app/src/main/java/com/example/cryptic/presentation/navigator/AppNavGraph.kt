package com.example.cryptic.presentation.navigator

import ChatScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cryptic.di.LocalPublicProfileViewModel
import com.example.cryptic.presentation.ResetPassword.ResetPasswordScreen
import com.example.cryptic.presentation.login.LoginScreen
import com.example.cryptic.presentation.main.HomeScreen
import com.example.cryptic.presentation.main.ProfileScreen
import com.example.cryptic.presentation.main.PublicProfileScreen
import com.example.cryptic.presentation.main.SettingsScreen
import com.example.cryptic.presentation.main.UserSearchScreen
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
        composable("search") {
            UserSearchScreen(navController)
        }

        composable(
            "chat/{recipientId}",
            arguments = listOf(navArgument("recipientId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipientId = backStackEntry.arguments?.getInt("recipientId") ?: -1
            ChatScreen(recipientId = recipientId, navController = navController)
        }

        composable(
            "public_profile/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            PublicProfileScreen(
                navController = navController,
                userId = backStackEntry.arguments?.getInt("userId") ?: -1
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    val navController = rememberNavController()
    AppNavGraph(navController = navController)
}