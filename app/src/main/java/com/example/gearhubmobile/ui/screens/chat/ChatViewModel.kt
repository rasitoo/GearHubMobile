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
import com.example.gearhubmobile.data.models.UserReduction
import com.example.gearhubmobile.data.repositories.ChatRepository
import com.example.gearhubmobile.data.repositories.ProfileRepository
import com.example.gearhubmobile.data.repositories.UserChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository,
    private val userRepository: ProfileRepository,
    private val userChatRepository: UserChatRepository,
) : ViewModel() {

    var chatList by mutableStateOf<List<Chat>>(emptyList())
    private val _users = MutableStateFlow<List<UserReduction>>(emptyList())
    val usersSelected = mutableStateListOf<UserReduction>()
    val users: StateFlow<List<UserReduction>> = _users

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

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

    fun loadChats() {
        viewModelScope.launch {
            isLoading = true
            try {
                chatList = repository.getChats()
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
}