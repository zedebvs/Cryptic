package com.example.cryptic.data.api.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
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
    val lastonline: String
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

@Serializable
data class ChatItem(
    val id: String,
    val text: String,
    val timestamp: String,
    val status: String,
    val sender_id: Int,
    val recipient_id: Int,
    val profile_id: Int,
    val name: String,
    val avatar: String,
    val profile_status: String?,
    val online: Int,
    val lastonline: String
)

@Serializable
data class MessageItem(
    val id: String,
    val sender_id: Int,
    val recipient_id: Int,
    val text: String,
    val timestamp: String,
    val status: String,
    val is_edited: Boolean,
    val reaction: String? = null,
    val message_type: String = "text",
    val attachment_url: String? = null
)
@Serializable
data class ProfileUser(
    val profile_id: Int,
    val name: String,
    val avatar: String,
    val profile_status: String?,
    val online: Int,
    val lastonline: String
)

