package com.example.cryptic

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.cryptic.Network.RetrofitClient
import com.example.cryptic.data.local.TokenManager
import com.example.cryptic.data.repository.AuthRepository
import com.example.cryptic.data.repository.RegisterRepository
import com.example.cryptic.di.LocalAuthRepository
import com.example.cryptic.di.LocalTokenManager
import com.example.cryptic.di.LocalRegistrationRepository
import com.example.cryptic.di.LocalMainViewModel
import com.example.cryptic.presentation.login.LoginScreen
import com.example.cryptic.presentation.main.MainViewModel
import com.example.cryptic.presentation.navigator.AppNavGraph


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val tokenManager = TokenManager(this)
        val apiService = RetrofitClient.getApiServiceWithRefresh(tokenManager)
        val authRepository = AuthRepository(apiService, tokenManager)
        val registerRepository = RegisterRepository(apiService)

        setContent {
            CompositionLocalProvider(
                LocalMainViewModel provides MainViewModel(authRepository),
                LocalRegistrationRepository provides registerRepository,
                LocalAuthRepository provides authRepository,
                LocalTokenManager provides tokenManager
            ) {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}