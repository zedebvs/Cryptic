package com.example.cryptic.data.api.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class LoginResponse(
    @SerializedName("tokens") val tokens: Tokens,
    @SerializedName("public_profile") val publicProfile: PublicProfile
)

data class Tokens(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String
)

data class PublicProfile(
    val id: Int,
    val name: String,
    val avatar: String,
    val status: String?,
    val online: Int,
    val lastOnline: String?
)

data class PrivateProfile(
    val id: Int,
    val name: String,
    val email: String,
    val avatar: String,
    val status: String?,
    val online: Int
)

data class RefreshRequest(val refreshToken: String)
data class RefreshResponse(val accessToken: String)

data class UpdateStatus(val status: String) // отправляем
data class NewStatus(val status: String)    // получаем

data class UserData(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val isOnline: Boolean
)