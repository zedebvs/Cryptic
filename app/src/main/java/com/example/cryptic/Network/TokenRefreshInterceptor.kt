package com.example.cryptic.Network

import com.example.cryptic.data.api.ApiService
import com.example.cryptic.data.api.models.RefreshRequest
import com.example.cryptic.data.local.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class TokenRefreshInterceptor(
    private val tokenManager: TokenManager,
    private val apiService: ApiService
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val response = chain.proceed(request)

        if (response.code == 401) {
            synchronized(this) {
                val refreshToken = tokenManager.getRefreshToken()
                if (refreshToken != null) {
                    val refreshResponse = runBlocking { // error runBlocking {
                        apiService.refreshToken(RefreshRequest(refreshToken)).execute() // error execute()
                    }

                    if (refreshResponse.isSuccessful) { // isSuccessful) error
                        val newAccessToken = refreshResponse.body()!!.accessToken //error .body()!!.accessToken
                        tokenManager.saveAccessToken(newAccessToken)
                        val newRequest = request.newBuilder()
                            .header("Authorization", "Bearer $newAccessToken")
                            .build()
                        response.close()
                        return chain.proceed(newRequest)
                    } else {
                        tokenManager.clearTokens()
                    }
                }
            }
        }

        return response
    }
}