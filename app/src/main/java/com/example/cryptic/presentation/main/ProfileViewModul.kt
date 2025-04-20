package com.example.cryptic.presentation.main

import android.R.id.message
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptic.data.api.models.PrivateProfile
import com.example.cryptic.data.repository.AuthRepository
import com.example.cryptic.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _profile = MutableStateFlow<PrivateProfile?>(null)
    val profile: StateFlow<PrivateProfile?> = _profile

    fun setProfile(privateProfile: PrivateProfile) {
        _profile.value = privateProfile
    }
    fun logout() {
        authRepository.logout()
    }

    fun updateStatus(newStatus: String) {
        viewModelScope.launch {
            val result = profileRepository.updateStatus(newStatus)
            if (result.isSuccess) {
                _profile.value = _profile.value?.copy(status = newStatus)
            }
        }
    }

    fun uploadAvatar(file: MultipartBody.Part) {
        viewModelScope.launch {
            var count = 0
            val success = profileRepository.uploadAvatar(file)
            if (success) {
                fetchProfile()
            } else {
                while (count < 2){
                    profileRepository.uploadAvatar(file)
                    count++
                }
                message?: "Неизвестная ошибка"
            }
        }
    }
    fun fetchProfile() {
        viewModelScope.launch {
            val result = profileRepository.getPrivateProfile()
            if (result.isSuccess) {
                _profile.value = result.getOrNull()
            } else {
                _profile.value = null
            }
        }
    }

    companion object
}