package com.example.cryptic.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.cryptic.data.api.ApiService
import com.example.cryptic.data.api.models.RefreshRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.runBlocking

class TokenManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_tokens",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    //fun saveAccessToken(token: String) = prefs.edit().putString("access_token", token).apply()
    private val _tokenUpdatedEvent = MutableSharedFlow<Unit>()
    val tokenUpdatedEvent = _tokenUpdatedEvent.asSharedFlow()

    fun saveAccessToken(token: String) {
        prefs.edit().putString("access_token", token).apply()
        runBlocking { _tokenUpdatedEvent.emit(Unit) }
    }
    fun getAccessToken(): String? = prefs.getString("access_token", null)

    fun saveRefreshToken(token: String) = prefs.edit().putString("refresh_token", token).apply()
    fun getRefreshToken(): String? = prefs.getString("refresh_token", null)

    fun clearTokens() = prefs.edit().clear().apply()

    fun refreshTokenBlocking(apiService: ApiService): String {
        val refreshToken = getRefreshToken() ?: throw Exception("No refresh token")

        val response = runBlocking {
            apiService.refreshToken(RefreshRequest(refreshToken)).execute()
        }

        if (!response.isSuccessful) {
            clearTokens()
            throw Exception("Refresh failed: ${response.code()}")
        }

        val newAccessToken = response.body()?.accessToken ?: throw Exception("No access token")
        saveAccessToken(newAccessToken)
        return newAccessToken
    }

}