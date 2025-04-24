package com.example.cryptic.presentation.main

import ChatRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptic.data.api.models.ChatItem
import com.example.cryptic.data.api.models.PublicProfile
import com.example.cryptic.data.repository.AuthRepository
import com.example.cryptic.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _profile = MutableStateFlow<PublicProfile?>(null)
    val profile: StateFlow<PublicProfile?> = _profile

    private val _chats = MutableStateFlow<List<ChatItem>>(emptyList())
    val chats: StateFlow<List<ChatItem>> = _chats

    fun setProfile(publicProfile: PublicProfile) {
        _profile.value = publicProfile
    }

    fun logout() {
        authRepository.logout()
    }

    fun fetchProfile() {
        viewModelScope.launch {
            val result = profileRepository.getPublicProfile()
            if (result.isSuccess) {
                _profile.value = result.getOrNull()
            } else {
                _profile.value = null
            }
        }
    }

    init {
        viewModelScope.launch {
            chatRepository.chats.collect {
                _chats.value = it
            }
        }
    }

    fun requestChats() {
        chatRepository.requestChats()
    }

}