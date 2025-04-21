package com.example.cryptic.data.api.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("name") val name: String?,
    @SerializedName("password") val password: String?
)