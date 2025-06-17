package com.example.gearhubmobile.ui.screens.chat

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gearhubmobile.data.models.Chat
import com.example.gearhubmobile.data.models.Message
import com.example.gearhubmobile.data.models.UserReduction
import com.example.gearhubmobile.data.repositories.ChatRepository
import com.example.gearhubmobile.data.repositories.ProfileRepository
import com.example.gearhubmobile.data.repositories.UserChatRepository
import com.example.gearhubmobile.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository,
    private val userRepository: ProfileRepository,
    private val userChatRepository: UserChatRepository,
    private val sessionManager: SessionManager
) : ViewModel() {


    private val _chatList = MutableStateFlow<List<Chat>>(emptyList())
    val chatList: StateFlow<List<Chat>> = _chatList

    private val _users = MutableStateFlow<List<UserReduction>>(emptyList())
    val usersSelected = mutableStateListOf<UserReduction>()
    val users: StateFlow<List<UserReduction>> = _users
    var currentUserId by mutableStateOf<String?>(null)
    private var _chat = MutableStateFlow<Chat?>(null)
    val chat: StateFlow<Chat?> = _chat
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)


    fun connectToChatList(
    ) {
        repository.connect(
            onChatDeleted = { chatId ->
                _chatList.value = _chatList.value.filter { it.id != chatId }
//                onChatDeleted(chatId)
            },
            onChatUpdated = { updatedChat ->
                _chatList.value = _chatList.value.map { if (it.id == updatedChat.id) updatedChat else it }
//                onChatUpdated(updatedChat)
            }
        )
    }

    fun loadUsers() {
        viewModelScope.launch {
            isLoading = true
            try {
                _users.value = userRepository.getAllUsers()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun getCurrentUserId() {
        viewModelScope.launch {
            currentUserId = sessionManager.getUserId()
        }
    }

    fun loadChats() {
        viewModelScope.launch {
            isLoading = true
            try {
                _chatList.value = repository.getChats()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun loadChat(id: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                _chat.value = repository.getChatById(id)
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun createChat(name: String) {
        viewModelScope.launch {
            try {
                var result = repository.createChat(name)
                if (result.code() == 201)
                    for (user: UserReduction in usersSelected)
                        userChatRepository.createUserChat(user.userId, result.body()?.id ?: "")

            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

    fun editChat(name: String, id: String) {
        viewModelScope.launch {
            try {
                repository.updateChat(id, name)
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

    fun deleteChat(id: String) {
        viewModelScope.launch {
            try {
                var result = repository.deleteChat(id)
                if (result.isSuccessful)
                    loadChats()
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }
}