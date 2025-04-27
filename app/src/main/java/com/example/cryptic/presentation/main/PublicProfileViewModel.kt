package com.example.cryptic.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptic.data.api.models.PublicProfile
import com.example.cryptic.data.repository.BaseRepository
import com.example.cryptic.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PublicProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _profile = MutableStateFlow<PublicProfile?>(null)
    val profile: StateFlow<PublicProfile?> = _profile

    fun loadUserProfile(userId: Int) {
        viewModelScope.launch {
            val result = repository.getUserProfile(userId)
            if (result.isSuccess) {
                _profile.value = result.getOrNull()
            } else {
                _profile.value = null
            }
        }
    }
}