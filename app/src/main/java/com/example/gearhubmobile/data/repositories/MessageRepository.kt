package com.example.gearhubmobile.data.repositories

import com.example.gearhubmobile.data.apirest.MessageApi
import com.example.gearhubmobile.data.models.CreateMessageRequest
import com.example.gearhubmobile.data.models.Message
import com.example.gearhubmobile.data.models.UpdateMessageRequest
import retrofit2.Response
import javax.inject.Inject

/**
 * @author Rodrigo
 * @date 29 mayo, 2025
 */
class MessageRepository @Inject constructor(private val api: MessageApi) {

    suspend fun getMessages(): List<Message> {
        return api.getMessages()
    }

    suspend fun getMessageById(id: String): Message {
        return api.getMessageById(id)
    }

    suspend fun createMessage(content: String, chatId: String): Response<Unit> {
        val messageRequest = CreateMessageRequest(content, chatId)
        return api.createMessage(messageRequest)
    }

    suspend fun deleteMessage(id: String): Response<Unit> {
        return api.deleteMessage(id)
    }

    suspend fun updateMessage(id: String, content: String): Response<Unit> {
        val messageRequest = UpdateMessageRequest(content)

        return api.updateMessage(id, messageRequest)
    }
}