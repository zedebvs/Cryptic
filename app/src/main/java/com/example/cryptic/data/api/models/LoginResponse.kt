package com.example.cryptic.data.api.models

import com.google.gson.annotations.SerializedName

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
    val status: String?
)

data class RefreshRequest(val refreshToken: String)
data class RefreshResponse(val accessToken: String)