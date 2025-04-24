package com.example.cryptic.domain.model

data class ChatItemData(
    val avatarRes: String,
    val nickName: String,
    val lastMessage: String,
    val time: String,
    val isUnread: Boolean,
    val isMineLastMessage: Boolean,
    val isLastMessageRead: Boolean,
    val isOnline : Int
)