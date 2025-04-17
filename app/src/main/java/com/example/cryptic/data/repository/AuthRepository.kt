package com.example.cryptic.data.repository

import com.example.cryptic.Network.RetrofitClient
import com.example.cryptic.data.api.models.LoginResponse
import com.example.cryptic.data.api.models.LoginRequest
import com.example.cryptic.data.api.models.RegistrationRequest
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.IOException

class AuthRepository {
    private val api = RetrofitClient.apiService

    private suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<T>
    ): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall()
                if (response.isSuccessful) {
                    Result.success(response.body()!!)
                } else {
                    val errorMsg = parseError(response.errorBody())
                    Result.failure(Exception(errorMsg ?: "Unknown error"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return safeApiCall { api.login(LoginRequest(email, password)) }
    }

    suspend fun register(
        email: String,
        name: String,
        password: String
    ): Result<Unit> {
        return safeApiCall { api.register(RegistrationRequest(email, name, password)) }
    }

    private fun parseError(errorBody: ResponseBody?): String? {
        return try {
            errorBody?.string()?.let { json ->
                try {
                    JsonParser.parseString(json)
                        .asJsonObject["detail"]
                        .asString
                } catch (e: Exception) {
                    json
                }
            }
        } catch (e: IOException) {
            null
        }
    }
}