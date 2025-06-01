package com.example.gearhubmobile.ui.screens.message

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gearhubmobile.data.models.Message
import com.example.gearhubmobile.data.repositories.MessageRepository
import com.example.gearhubmobile.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Base64
import org.json.JSONObject

@HiltViewModel
class MessageViewModel @Inject constructor(private val repository: MessageRepository,    private val sessionManager: SessionManager
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    val currentUserId = sessionManager.token.map { extractUserIdFromToken(it) }.stateIn(viewModelScope, SharingStarted.Eagerly, "")

    fun connectToChat(chatId: String) {
        viewModelScope.launch {
            val history = repository.getMessageById(chatId)
            _messages.value = listOf(history)

            repository.connect(chatId) { newMessage ->
                _messages.update { it + newMessage }
            }
        }
    }

    fun sendMessage(chatId: String, content: String) {
        viewModelScope.launch {
            repository.createMessage(content, chatId)
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
        val payload = String(Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP))
        val json = JSONObject(payload)
        return json.optString("userId", "")
    }
}