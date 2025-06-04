package com.example.cryptic.data.repository

import com.example.cryptic.Network.RetrofitClient
import com.example.cryptic.data.api.ApiService
import com.example.cryptic.data.api.models.LoginResponse
import com.example.cryptic.data.api.models.LoginRequest
import com.example.cryptic.data.api.models.PublicKey
import com.example.cryptic.data.api.models.PublicProfile
import com.example.cryptic.data.api.models.RegistrationRequest
import com.example.cryptic.data.local.TokenManager
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import android.util.Base64
import com.example.cryptic.data.Crypto.UserKeyStore

class AuthRepository(
    private val apiService: ApiService,
    private val tokenManager: TokenManager,
    private val userKeyStore: UserKeyStore
) {
    suspend fun login(email: String, password: String): Result<PublicProfile> {
        return try {
            val response = apiService.login(LoginRequest(email, password))

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null && body.tokens.accessToken != null && body.tokens.refreshToken != null) {
                    tokenManager.saveAccessToken(body.tokens.accessToken)
                    tokenManager.saveRefreshToken(body.tokens.refreshToken)
                    Result.success(body.publicProfile)

                } else {
                    Result.failure(Exception("Токены не получены"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = parseErrorMessage(errorBody)
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun fetchEncryptedAESKey(publicKey: String): ByteArray? {
        return try {
            val response = apiService.getMyKey(PublicKey(publicKey))
            if (response.isSuccessful) {
                val encryptedBase64 = response.body()?.key ?: return null
                Base64.decode(encryptedBase64, Base64.DEFAULT)
            } else null
        } catch (e: Exception) {
            null
        }
    }
    fun logout() {
        tokenManager.clearTokens()
        userKeyStore.clearKey()
    }

    private fun parseErrorMessage(errorBody: String?): String {
        return try {
            val jsonObject = JSONObject(errorBody)
            jsonObject.optString("detail", "Неизвестная ошибка")
        } catch (e: Exception) {
            "Неизвестная ошибка"
        }
    }
}

class RegisterRepository(
    private val apiService: ApiService
) {
    suspend fun register(email: String, name: String, password: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.register(RegistrationRequest(email, name, password))
                if (response.isSuccessful) {
                    val body = response.body()?.string()
                    if (body == "true" || body == "True") {
                        Result.success(Unit)
                    } else {
                        Result.failure(Exception("Неожиданный ответ от сервера: $body"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorDetail = try {
                        JSONObject(errorBody).getString("detail")
                    } catch (ex: Exception) {
                        errorBody ?: "Неизвестная ошибка"
                    }
                    Result.failure(Exception(errorDetail))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}