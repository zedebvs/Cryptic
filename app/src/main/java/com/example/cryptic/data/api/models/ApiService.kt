package com.example.cryptic.data.api

import com.example.cryptic.data.api.models.LoginResponse
import com.example.cryptic.data.api.models.LoginRequest
import com.example.cryptic.data.api.models.NewStatus
import com.example.cryptic.data.api.models.PrivateProfile
import com.example.cryptic.data.api.models.PublicProfile
import com.example.cryptic.data.api.models.RefreshRequest
import com.example.cryptic.data.api.models.RefreshResponse
import com.example.cryptic.data.api.models.RegistrationRequest
import com.example.cryptic.data.api.models.UpdateStatus
import okhttp3.MultipartBody

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("/auth/login")
    suspend fun login(@Body credentials: LoginRequest): Response<LoginResponse>

    @POST("auth/registration")
    suspend fun register(@Body request: RegistrationRequest): Response<ResponseBody>

    @POST("/auth/refresh")
    fun refreshToken(@Body request: RefreshRequest): Call<RefreshResponse>

    @GET("/profiles/publicProfile")
    suspend fun getPublicProfile(): Response<PublicProfile>

    @GET("/profiles/privateProfile")
    suspend fun getPrivateProfile(): Response<PrivateProfile>

    @POST("/profiles/Status")
    suspend fun updateStatus(@Body status: UpdateStatus): Response<NewStatus>

    @Multipart
    @POST("/profiles/uploadAvatar")
    suspend fun uploadAvatar(
        @Part file: MultipartBody.Part
    ): Response<Unit>

    @GET("/profiles/search")
    suspend fun searchUsers(
        @Query("query") query: String
    ): Response<List<PublicProfile>>

    @GET("/profiles/user_profile/{userId}")
    suspend fun getUserProfile(
        @Path("userId") userId: Int
    ): Response<PublicProfile>
}

