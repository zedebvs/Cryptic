package com.example.cryptic.Network

import android.util.Log
import com.example.cryptic.data.api.ApiService
import com.example.cryptic.data.local.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    private var onRawMessageReceived: ((String) -> Unit)? = null

    private lateinit var apiService: ApiService

    private var pingJob: Job? = null

    private fun startPinging() {
        pingJob = CoroutineScope(Dispatchers.IO).launch {
            while (isConnected) {
                delay(30_000)
                send("""{"action": "ping"}""")
            }
        }
    }

    private fun stopPinging() {
        pingJob?.cancel()
        pingJob = null
    }
    fun init(tokenManager: TokenManager, api: ApiService) {
        this.tokenManager = tokenManager
        this.apiService = api
        client = OkHttpClient()
    }

    fun setOnMessageReceivedListener(listener: (String) -> Unit) {
        onMessageReceived = listener
    }

    fun setOnRawMessageReceivedListener(listener: (String) -> Unit) {
        onRawMessageReceived = listener
    }
    fun isReady(): Boolean = isConnected

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
            .url("wss://192.168.0.222/ws/chat?token=$accessToken")
            .build()

        webSocket = client.newWebSocket(request, socketListener)
    }

    fun send(message: String) {
        webSocket?.send(message)
    }

    fun close() {
        webSocket?.close(1000, "Client closed connection")
        isConnected = false

        stopPinging()
    }

    private val socketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            isConnected = true

            startPinging()
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            onMessageReceived?.invoke(text)
            onRawMessageReceived?.invoke(text)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            isConnected = false
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            isConnected = false

            if (response?.code == 401) {
                reconnectWithNewToken()
            }
        }
    }

    private fun reconnectWithNewToken() {

        val newToken = tokenManager.refreshTokenBlocking(apiService)

        val request = Request.Builder()
            .url("wss://192.168.0.222/ws/chat?token=$newToken")
            .build()

        webSocket = client.newWebSocket(request, socketListener)
    }
}
