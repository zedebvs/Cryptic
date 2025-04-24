import android.util.Log
import com.example.cryptic.Network.WebSocketClient
import com.example.cryptic.data.api.ApiService
import com.example.cryptic.data.api.models.ChatItem
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

class ChatRepository(
    private val tokenManager: TokenManager,
    private val apiService: ApiService
) {
    private val _chats = MutableStateFlow<List<ChatItem>>(emptyList())
    val chats: StateFlow<List<ChatItem>> = _chats

    init {
        WebSocketClient.init(tokenManager, apiService)
        WebSocketClient.setOnMessageReceivedListener { message ->
            handleWebSocketMessage(message)
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
                        _chats.value = chats
                    }
                }

                "new_message" -> {
                    if (data != null && data !is JsonNull) {
                        val chatItem = Json.decodeFromJsonElement<ChatItem>(data)
                        updateChats(chatItem)
                    }
                }

                else -> {
                    Log.w("ChatRepository", "Unknown action: $action")
                }
            }

        } catch (e: Exception) {
            Log.e("ChatRepository", "Error parsing WebSocket message", e)
        }
    }

    fun requestChats() {
        val request = """{ "action": "giveChats" }"""
        WebSocketClient.send(request)
    }

    private fun updateChats(newChatItem: ChatItem) {
        val currentChats = _chats.value.toMutableList()

        currentChats.removeAll { it.profile_id == newChatItem.profile_id }
        currentChats.add(0, newChatItem)

        _chats.value = currentChats
    }

}