package com.example.cryptic.data.api

import com.example.cryptic.data.api.models.LoginResponse
import com.example.cryptic.data.api.models.LoginRequest
import com.example.cryptic.data.api.models.RegistrationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/auth/login")
    suspend fun login(@Body credentials: LoginRequest): Response<LoginResponse>

    @POST("/auth/registration")
    suspend fun register(@Body credentials: RegistrationRequest): Response<Unit>
}
