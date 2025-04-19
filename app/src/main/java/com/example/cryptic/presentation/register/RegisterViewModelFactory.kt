package com.example.cryptic.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cryptic.data.repository.RegisterRepository
import com.example.cryptic.ui.registration.RegisterViewModel

class RegisterViewModelFactory(
    private val registerRepository: RegisterRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(registerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}