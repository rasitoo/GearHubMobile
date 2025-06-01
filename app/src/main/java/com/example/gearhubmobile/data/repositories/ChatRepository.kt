package com.example.gearhubmobile.data.repositories

import com.example.gearhubmobile.data.apirest.ChatApi
import com.example.gearhubmobile.data.models.Chat
import com.example.gearhubmobile.data.models.CreateChatRequest
import com.example.gearhubmobile.data.models.UpdateChatRequest
import retrofit2.Response
import javax.inject.Inject

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
class ChatRepository @Inject constructor(private val api: ChatApi) {

    suspend fun getChats(): List<Chat> {
        return api.getChats()
    }

    suspend fun getChatById(id: String): Chat {
        return api.getChatById(id)
    }

    suspend fun createChat(name: String): Response<Unit> {
        val chatRequest = CreateChatRequest(name)
        return api.createChat(chatRequest)
    }

    suspend fun deleteChat(id: String): Response<Unit> {
        return api.deleteChat(id)
    }

    suspend fun updateChat(id: String, name: String) {
        val chatRequest = UpdateChatRequest(name)
        return api.updateChat(id, chatRequest)
    }
}
