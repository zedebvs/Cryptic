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
        var response = chain.proceed(request)

        if (response.code == 401) {
            synchronized(this) {
                response.close() // закрываем старый response

                val refreshToken = tokenManager.getRefreshToken()
                if (refreshToken != null) {
                    val refreshResponse = runBlocking {
                        apiService.refreshToken(RefreshRequest(refreshToken)).execute()
                    }

                    if (refreshResponse.isSuccessful) {
                        val newAccessToken = refreshResponse.body()?.accessToken

                        if (newAccessToken != null) {
                            tokenManager.saveAccessToken(newAccessToken)

                            val newRequest = request.newBuilder()
                                .header("Authorization", "Bearer $newAccessToken")
                                .build()

                            return chain.proceed(newRequest)
                        } else {
                            // если refresh прошел, но token не пришёл
                            tokenManager.clearTokens()
                        }
                    } else {
                        // refresh не успешен
                        tokenManager.clearTokens()
                    }
                }
            }
        }

        return response
    }
}