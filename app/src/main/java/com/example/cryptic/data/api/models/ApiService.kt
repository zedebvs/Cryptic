package com.example.cryptic.data.api

import com.example.cryptic.data.api.models.LoginResponse
import com.example.cryptic.data.api.models.LoginRequest
import com.example.cryptic.data.api.models.RefreshRequest
import com.example.cryptic.data.api.models.RefreshResponse
import com.example.cryptic.data.api.models.RegistrationRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/auth/login")
    suspend fun login(@Body credentials: LoginRequest): Response<LoginResponse>

    @POST("auth/registration")
    suspend fun register(@Body request: RegistrationRequest): Response<ResponseBody>

    @POST("/auth/refresh")
    fun refreshToken(@Body request: RefreshRequest): Call<RefreshResponse>
}