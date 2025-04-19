package com.example.cryptic.presentation.main

import androidx.lifecycle.ViewModel
import com.example.cryptic.data.api.models.PublicProfile
import com.example.cryptic.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _profile = MutableStateFlow<PublicProfile?>(null)
    val profile: StateFlow<PublicProfile?> = _profile

    fun setProfile(publicProfile: PublicProfile) {
        _profile.value = publicProfile
    }

    fun logout() {
        authRepository.logout()
    }
}