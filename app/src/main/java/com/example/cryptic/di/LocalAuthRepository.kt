package com.example.cryptic.di

import androidx.compose.runtime.staticCompositionLocalOf
import com.example.cryptic.data.repository.AuthRepository
import com.example.cryptic.data.repository.RegisterRepository
import com.example.cryptic.data.local.TokenManager
import com.example.cryptic.data.repository.SocketRepository
import com.example.cryptic.presentation.main.MainViewModel
import com.example.cryptic.presentation.main.ProfileViewModel
import com.example.cryptic.presentation.main.SettingsViewModel

val LocalMainViewModel = staticCompositionLocalOf<MainViewModel> {
    error("No MainViewModel provided")
}

val LocalTokenManager = staticCompositionLocalOf<TokenManager> {
    error("No TokenManager provided")
}

val LocalAuthRepository = staticCompositionLocalOf<AuthRepository> {
    error("No AuthRepository provided")
}

val LocalRegistrationRepository = staticCompositionLocalOf<RegisterRepository> {
    error("No RegistrationRepository provided")
}

val LocalProfileViewModel = staticCompositionLocalOf<ProfileViewModel> {
    error("No ProfileViewModel provided")
}

val LocalSettingsViewModel = staticCompositionLocalOf<SettingsViewModel> {
    error("No SocketRepository provided")
}
