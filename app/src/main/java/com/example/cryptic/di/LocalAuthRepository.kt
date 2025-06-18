package com.example.cryptic.di

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.cryptic.data.Crypto.UserKeyStore
import com.example.cryptic.data.repository.AuthRepository
import com.example.cryptic.data.repository.RegisterRepository
import com.example.cryptic.data.local.TokenManager
import com.example.cryptic.data.repository.SocketRepository
import com.example.cryptic.presentation.main.ChatViewModel
import com.example.cryptic.presentation.main.MainViewModel
import com.example.cryptic.presentation.main.ProfileViewModel
import com.example.cryptic.presentation.main.PublicProfileViewModel
import com.example.cryptic.presentation.main.SearchViewModel
import com.example.cryptic.presentation.main.SettingsViewModel


val LocalMainViewModel = compositionLocalOf <MainViewModel> {
    error("No MainViewModel provided")
}

val LocalTokenManager = staticCompositionLocalOf<TokenManager> {
    error("No TokenManager provided")
}

val LocalAuthRepository = compositionLocalOf<AuthRepository> {
    error("No AuthRepository provided")
}

val LocalRegistrationRepository = compositionLocalOf<RegisterRepository> {
    error("No RegistrationRepository provided")
}

val LocalProfileViewModel = compositionLocalOf<ProfileViewModel> {
    error("No ProfileViewModel provided")
}

val LocalSettingsViewModel = compositionLocalOf<SettingsViewModel> {
    error("No SocketRepository provided")
}

val LocalSearchViewModel = compositionLocalOf<SearchViewModel> {
    error("No SocketRepository provided")
}

val LocalPublicProfileViewModel = compositionLocalOf<PublicProfileViewModel> {
    error("No PublicProfileViewModel provided")
}

val LocalChatViewModel = compositionLocalOf<ChatViewModel> {
    error("No ChatViewModel provided")
}

val LocalUserKeyStore = compositionLocalOf<UserKeyStore> {
    error("No UserKeyStore provided")
}