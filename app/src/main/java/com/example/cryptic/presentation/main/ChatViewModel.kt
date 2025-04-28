package com.example.cryptic.presentation.main

import ChatRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptic.data.api.models.MessageItem
import com.example.cryptic.data.api.models.ProfileUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<MessageItem>>(emptyList())
    val messages: StateFlow<List<MessageItem>> = _messages.asStateFlow()

    private val _profile = MutableStateFlow<ProfileUser?>(null)
    val profile: StateFlow<ProfileUser?> = _profile.asStateFlow()

    init {
        chatRepository.setOnMessagesReceivedListener { messageList, profileUser ->
            viewModelScope.launch {
                _messages.value = messageList.toList()
                _profile.value = profileUser
            }
        }
    }
    fun markMessageAsRead(messageId: String) {
        chatRepository.markMessageAsRead(messageId)
    }
    fun sendMessage(recipientId: Int, messageText: String) {
        chatRepository.sendMessage(recipientId, messageText)
    }

    fun requestMessages(recipientId: Int) {
        chatRepository.requestMessages(recipientId)
    }

    fun clearChat() {
        _messages.value = emptyList()
        _profile.value = null
    }
}