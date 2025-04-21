package com.example.cryptic.Network

import android.util.Log
import com.example.cryptic.data.api.ApiService
import com.example.cryptic.data.local.TokenManager
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

object WebSocketClient {
    private var webSocket: WebSocket? = null
    private lateinit var client: OkHttpClient
    private lateinit var tokenManager: TokenManager

    private var isConnected = false

    private var onMessageReceived: ((String) -> Unit)? = null

    private lateinit var apiService: ApiService

    fun init(tokenManager: TokenManager, api: ApiService) {
        this.tokenManager = tokenManager
        this.apiService = api
        client = OkHttpClient()
    }

    fun setOnMessageReceivedListener(listener: (String) -> Unit) {
        onMessageReceived = listener
    }

    fun connect() {
        if (isConnected) {
            Log.d("WebSocket", "Already connected")
            return
        }

        val accessToken = tokenManager.getAccessToken() ?: run {
            Log.e("WebSocket", "No token found")
            return
        }

        val request = Request.Builder()
            .url("ws://192.168.0.200:8000/ws/chat?token=$accessToken")
            .build()

        webSocket = client.newWebSocket(request, socketListener)
    }

    fun send(message: String) {
        webSocket?.send(message)
    }

    fun close() {
        webSocket?.close(1000, "Client closed connection")
        isConnected = false
    }

    private val socketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("WebSocket", "Connected")
            isConnected = true
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("WebSocket", "Message: $text")
            onMessageReceived?.invoke(text)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("WebSocket", "Closing: $code $reason")
            isConnected = false
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.e("WebSocket", "Failure: ${t.message}")
            isConnected = false

            if (response?.code == 401) {
                reconnectWithNewToken()
            }
        }
    }

    private fun reconnectWithNewToken() {
        Log.d("WebSocket", "Attempting reconnect with refreshed token")

        val newToken = tokenManager.refreshTokenBlocking(apiService)

        val request = Request.Builder()
            .url("ws://192.168.0.200:8000/ws/chat?token=$newToken")
            .build()

        webSocket = client.newWebSocket(request, socketListener)
    }
}
