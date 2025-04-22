package com.example.cryptic.data.repository

import com.example.cryptic.Network.WebSocketClient
import com.example.cryptic.data.api.ApiService
import com.example.cryptic.data.local.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SocketRepository(
    private val tokenManager: TokenManager,
    apiService: ApiService
) {
    private val _incomingMessages = MutableStateFlow<String?>(null)
    val incomingMessages: StateFlow<String?> = _incomingMessages.asStateFlow()

    init {
        WebSocketClient.init(tokenManager, apiService)
        WebSocketClient.setOnMessageReceivedListener { message ->
            _incomingMessages.value = message
        }

        tokenManager.tokenUpdatedEvent
            .onEach {
                WebSocketClient.close()
                WebSocketClient.connect()
            }
            .launchIn(CoroutineScope(Dispatchers.IO))
    }

    fun connect() = WebSocketClient.connect()
    fun send(message: String) = WebSocketClient.send(message)
    fun close() = WebSocketClient.close()
}
