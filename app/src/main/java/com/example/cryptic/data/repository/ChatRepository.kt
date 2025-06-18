import android.util.Log
import com.example.cryptic.Network.WebSocketClient
import com.example.cryptic.data.Crypto.AESHelper
import com.example.cryptic.data.Crypto.UserKeyStore
import com.example.cryptic.data.api.ApiService
import com.example.cryptic.data.api.models.ChatItem
import com.example.cryptic.data.api.models.MessageItem
import com.example.cryptic.data.api.models.ProfileUser
import com.example.cryptic.data.local.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.buildJsonObject

class ChatRepository(
    private val tokenManager: TokenManager,
    private val apiService: ApiService,
    private val userKeyStore: UserKeyStore
) {
    private val _chats = MutableStateFlow<List<ChatItem>>(emptyList())
    val chats: StateFlow<List<ChatItem>> = _chats

    private val _messages = MutableStateFlow<List<MessageItem>>(emptyList())
    val messages: StateFlow<List<MessageItem>> = _messages

    private val _currentChatProfile = MutableStateFlow<ProfileUser?>(null)
    val currentChatProfile: StateFlow<ProfileUser?> = _currentChatProfile

    private var onMessagesReceivedListener: ((List<MessageItem>, ProfileUser) -> Unit)? = null

    fun setOnMessagesReceivedListener(listener: (List<MessageItem>, ProfileUser) -> Unit) {
        onMessagesReceivedListener = listener
    }

    init {
        WebSocketClient.init(tokenManager, apiService)
        WebSocketClient.setOnMessageReceivedListener { message ->
            handleWebSocketMessage(message)
        }
    }
    private fun decryptChatIfNeeded(chat: ChatItem): ChatItem {
        return if (chat.iv != null && chat.tag != null) {
            val hexKey = userKeyStore.getAesKey() ?: return chat
            try {
                val decryptedText = AESHelper.decrypt(
                    ciphertextBase64 = chat.text,
                    ivBase64 = chat.iv,
                    tagBase64 = chat.tag,
                    hexKey = hexKey
                )
                chat.copy(text = decryptedText)
            } catch (e: Exception) {
                chat.copy(text = "Зашифровано без возможности расшифрования")
            }
        } else {
            chat
        }
    }
    private fun decryptMessageIfNeeded(message: MessageItem): MessageItem {
        return if (message.iv != null && message.tag != null) {
            val hexKey = userKeyStore.getAesKey() ?: return message
            try {
                val decryptedText = AESHelper.decrypt(
                    ciphertextBase64 = message.text,
                    ivBase64 = message.iv,
                    tagBase64 = message.tag,
                    hexKey = hexKey
                )
                message.copy(text = decryptedText)
            } catch (e: Exception) {
                message.copy(text = "Зашифровано без возможности расшифрования")
            }
        } else {
            message
        }
    }

    suspend fun getChats(): Result<List<ChatItem>> {
        return Result.success(emptyList())
    }

    private fun handleWebSocketMessage(message: String) {
        try {
            val json = Json.parseToJsonElement(message).jsonObject
            val action = json["action"]?.jsonPrimitive?.contentOrNull
            val data = json["data"]

            when (action) {
                "chats" -> {
                    if (data != null && data !is JsonNull) {
                        val chats = Json.decodeFromJsonElement<List<ChatItem>>(data)
                        val decryptedChats = chats.map { decryptChatIfNeeded(it) }
                        _chats.value = decryptedChats.sortedByDescending { it.timestamp }
                    }
                }
                "message_sent" -> {
                    if (data != null && data !is JsonNull) {
                        val newMessage = decryptMessageIfNeeded(Json.decodeFromJsonElement<MessageItem>(data))
                        val updatedMessages = _messages.value.toMutableList().apply { add(newMessage) }
                        _messages.value = updatedMessages

                        _currentChatProfile.value?.let { profile ->
                            onMessagesReceivedListener?.invoke(updatedMessages, profile)
                        }
                    }
                }
                "message_read" -> {
                    if (data != null) {
                        val updatedMessage = decryptMessageIfNeeded(Json.decodeFromJsonElement<MessageItem>(data))
                        val updatedMessages = _messages.value.map {
                            if (it.id == updatedMessage.id) updatedMessage else it
                        }
                        _messages.value = updatedMessages

                        _currentChatProfile.value?.let { profile ->
                            onMessagesReceivedListener?.invoke(updatedMessages, profile)
                        }
                    }
                }
                "new_message" -> {
                    if (data != null && data !is JsonNull) {
                        val newMessage = decryptMessageIfNeeded(Json.decodeFromJsonElement<MessageItem>(data))
                        val updatedMessages = _messages.value.toMutableList().apply { add(newMessage) }
                        _messages.value = updatedMessages
//                        val newMessage = Json.decodeFromJsonElement<MessageItem>(data)
//                        val updatedMessages = _messages.value.toMutableList().apply { add(newMessage) }
//                        _messages.value = updatedMessages
                        _currentChatProfile.value?.let { profile ->
                            onMessagesReceivedListener?.invoke(updatedMessages, profile)
                        }
                    }
                }

                "updateChat" -> {
                    if (data != null && data !is JsonNull) {
                        val updatedChatItem = Json.decodeFromJsonElement<ChatItem>(data)
                        val decryptedChatItem = decryptChatIfNeeded(updatedChatItem)
                        updateChats(decryptedChatItem)
                    }
                }
                "messages_list" -> {
                    val profileJson = json["profile"]
                    if (data != null && profileJson != null) {
                        val messages = Json.decodeFromJsonElement<List<MessageItem>>(data)
                        val messagesDecrypted = messages.map { decryptMessageIfNeeded(it) }
                        val profileUser = Json.decodeFromJsonElement<ProfileUser>(profileJson)
                        _messages.value = messagesDecrypted
                        _currentChatProfile.value = profileUser

                        onMessagesReceivedListener?.invoke(messagesDecrypted, profileUser)
                    }
                }
                else -> {
                    Log.w("ChatRepository", "unknown action: $action")
                }
            }

        } catch (e: Exception) {
            Log.e("ChatRepository", "ошибка парсинга", e)
        }
    }

    fun requestChats() {
        val request = """{ "action": "giveChats" }"""
        WebSocketClient.send(request)
    }
    fun sendMessage(recipientId: Int, messageText: String) {
        val hexKey = userKeyStore.getAesKey()?: run { return }
        val (ciphertext, iv, tag) = AESHelper.encrypt(messageText, hexKey)

        val request = """{ "action": "sendMessage", "recipient_id": $recipientId, "message": "$ciphertext", "iv": "$iv", "tag": "$tag" }"""
        WebSocketClient.send(request)
    }
    fun markMessageAsRead(id: String) {
        val request = """{ "action": "mark_as_read", "message_id": "$id" }"""
        WebSocketClient.send(request)
    }
    fun requestMessages(recipientId: Int) {
        val request = """{ "action": "loadMessages", "recipient_id": $recipientId }"""
        WebSocketClient.send(request)
    }

    private fun updateChats(newChatItem: ChatItem) {
        val currentChats = _chats.value.toMutableList()

        currentChats.removeAll { it.profile_id == newChatItem.profile_id }
        currentChats.add(0, newChatItem)

        _chats.value = currentChats.sortedByDescending { it.timestamp }
    }

}