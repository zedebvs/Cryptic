package com.example.cryptic

import ChatRepository
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
import com.example.cryptic.data.repository.ProfileRepository
import com.example.cryptic.data.repository.RegisterRepository
import com.example.cryptic.data.repository.SocketRepository
import com.example.cryptic.di.LocalAuthRepository
import com.example.cryptic.di.LocalChatViewModel
import com.example.cryptic.di.LocalTokenManager
import com.example.cryptic.di.LocalRegistrationRepository
import com.example.cryptic.di.LocalMainViewModel
import com.example.cryptic.di.LocalProfileViewModel
import com.example.cryptic.di.LocalPublicProfileViewModel
import com.example.cryptic.di.LocalSearchViewModel
import com.example.cryptic.di.LocalSettingsViewModel
import com.example.cryptic.presentation.login.LoginScreen
import com.example.cryptic.presentation.main.ChatViewModel
import com.example.cryptic.presentation.main.MainViewModel
import com.example.cryptic.presentation.main.ProfileViewModel
import com.example.cryptic.presentation.main.PublicProfileViewModel
import com.example.cryptic.presentation.main.SearchViewModel
import com.example.cryptic.presentation.main.SettingsViewModel
import com.example.cryptic.presentation.navigator.AppNavGraph


class MainActivity : ComponentActivity() {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT

        val tokenManager = TokenManager(this)
        val apiService = RetrofitClient.getApiServiceWithRefresh(tokenManager)
        val authRepository = AuthRepository(apiService, tokenManager)
        val registerRepository = RegisterRepository(apiService)
        val profileRepository = ProfileRepository(apiService)
        val profileViewModel = ProfileViewModel(profileRepository, authRepository)
        val socketRepository = SocketRepository(tokenManager, apiService)
        val searchViewModel = SearchViewModel(profileRepository)
        val chatRepository = ChatRepository(tokenManager, apiService)
        val publicProfileViewModel = PublicProfileViewModel(profileRepository)
        val chatViewModel = ChatViewModel(chatRepository)

        val settingsViewModel = SettingsViewModel(socketRepository)
        //settingsViewModel.connect()

        setContent {
            CompositionLocalProvider(
                LocalMainViewModel provides MainViewModel(profileRepository, authRepository, chatRepository),
                LocalRegistrationRepository provides registerRepository,
                LocalAuthRepository provides authRepository,
                LocalTokenManager provides tokenManager,
                LocalProfileViewModel provides profileViewModel,
                LocalSettingsViewModel provides settingsViewModel,
                LocalSearchViewModel provides searchViewModel,
                LocalPublicProfileViewModel provides publicProfileViewModel,
                LocalChatViewModel provides chatViewModel
            ) {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        settingsViewModel.close()
    }
}