package com.example.cryptic.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptic.data.repository.SocketRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val socketRepository: SocketRepository
) : ViewModel() {
    val incomingMessages: StateFlow<String?> = socketRepository.incomingMessages

    fun changeNickname(name: String) {
        viewModelScope.launch {
            val messageJson = """
                {
                    "action": "changeName",
                    "name": "$name"
                }
            """.trimIndent()

            socketRepository.send(messageJson)
        }
    }

    fun clearIncomingMessage() {
        socketRepository.clearIncomingMessage()
    }

    fun connect() {
        socketRepository.connect()
    }

    fun close() {
        socketRepository.close()
    }
}