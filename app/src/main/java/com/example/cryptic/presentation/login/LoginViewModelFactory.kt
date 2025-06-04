package com.example.cryptic.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cryptic.data.Crypto.UserKeyStore
import com.example.cryptic.data.repository.AuthRepository

class LoginViewModelFactory(
    private val authRepository: AuthRepository,
    private val userKeyStore: UserKeyStore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authRepository, userKeyStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}