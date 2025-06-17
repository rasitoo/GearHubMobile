package com.example.gearhubmobile.ui.screens.message

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
import android.R
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gearhubmobile.data.models.Chat
import com.example.gearhubmobile.data.models.Message
import com.example.gearhubmobile.data.models.User
import com.example.gearhubmobile.data.models.UserReduction
import com.example.gearhubmobile.data.repositories.ChatRepository
import com.example.gearhubmobile.data.repositories.MessageRepository
import com.example.gearhubmobile.data.repositories.ProfileRepository
import com.example.gearhubmobile.data.repositories.UserChatRepository
import com.example.gearhubmobile.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val repository: MessageRepository,
    private val userChatRepository: UserChatRepository,
    private val chatRepository: ChatRepository,
    private val userRepository: ProfileRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages
    private val _message = MutableStateFlow<Message?>(null)
    val message: StateFlow<Message?> = _message
    val currentUserId = sessionManager.token.map { extractUserIdFromToken(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    private val _navigateToChatList = MutableStateFlow(false)
    val navigateToChatList: StateFlow<Boolean> = _navigateToChatList


    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users
    private val _chat = MutableStateFlow<Chat?>(null)
    val chat: StateFlow<Chat?> = _chat

    private var myConnectionId: String? = null

    fun connectToChat(chatId: Int) {
        viewModelScope.launch {
            val history = repository.getMessagesFiltered(chatId = chatId).sortedBy { it.sentAtDate }
            _messages.value = history

            repository.connect(
                chatId.toString(),
                onReceive = { newMessage ->
                    _messages.update { it + newMessage }
                },
                onUpdate = { updatedMessage ->
                    _messages.update { messages ->
                        messages.map { if (it.id == updatedMessage.id) updatedMessage else it }
                    }
                },
                onDelete = { id ->
                    _messages.update { messages: List<Message> ->
                        messages.filter { it.id != id }
                    }
                }
            )
            userChatRepository.connect(
                onUserJoined = { userConnectionId ->
                    viewModelScope.launch {
                        val userr = userRepository.getUserById(userConnectionId).getOrNull()
                        if (userr != null)
                            _users.update { it + userr }
                    }
                },
                onUserLeft = { userConnectionId ->
                    _users.update { users -> users.filter { it.id != userConnectionId } }
                    if (userConnectionId == myConnectionId) {
                        _navigateToChatList.value = true
                    }
                },
                onOwnConnectionId = { connectionId ->
                    myConnectionId = connectionId
                }
            )
        }
    }
    fun loadChat(id: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                _chat.value = chatRepository.getChatById(id)
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun loadUsers(id: String) {
        viewModelScope.launch {
            _users.value = emptyList()
            isLoading = true
            try {
                val temp = userChatRepository.getUserChats(id)
                Log.d("erase", temp.chatId.toString())
                temp.users.forEach { it ->
                    var result = userRepository.getUserById(it.userId.toString())
                    if (result.isSuccess)
                        result.getOrNull()?.let { user ->
                            _users.value = _users.value + user
                        }
                }
            } catch (e: Exception) {
                Log.e("erase", "Error en getUserChats: ${e.message}")

                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun sendMessage(chatId: String, content: String) {
        viewModelScope.launch {
            repository.createMessage(content, chatId)
        }
    }

    fun removeUserFromChat(chatId: String, userId: String) {
        viewModelScope.launch {
            userChatRepository.deleteUserChat(userId, chatId)
            loadUsers(chatId)
        }
    }

    fun editMessage(messageId: String, content: String) {
        viewModelScope.launch {
            repository.updateMessage(messageId, content)
        }
    }

    fun deleteMessage(messageId: String) {
        viewModelScope.launch {
            repository.deleteMessage(messageId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.disconnect()
    }

    fun extractUserIdFromToken(token: String?): String {
        if (token.isNullOrBlank()) return ""
        val parts = token.split(".")
        if (parts.size < 2) return ""
        val payload =
            String(Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP))
        val json = JSONObject(payload)
        return json.optString("nameid", "")
    }
}