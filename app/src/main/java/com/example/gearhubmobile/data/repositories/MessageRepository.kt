package com.example.gearhubmobile.data.repositories

import com.example.gearhubmobile.data.apirest.MessageApi
import com.example.gearhubmobile.data.models.CreateMessageRequest
import com.example.gearhubmobile.data.models.Message
import com.example.gearhubmobile.data.models.UpdateMessageRequest
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import retrofit2.Response
import javax.inject.Inject

/**
 * @author Rodrigo
 * @date 29 mayo, 2025
 */
class MessageRepository @Inject constructor(private val api: MessageApi) {
    private lateinit var hubConnection: HubConnection

    suspend fun connect(
        chatId: String,
        onReceive: (Message) -> Unit,
        onUpdate: (Message) -> Unit,
        onDelete: (String) -> Unit
    ) {
        hubConnection =
            HubConnectionBuilder.create("http://vms.iesluisvives.org:25003/hubs/messages")
                .build()

        hubConnection.on("ReceiveMessage", { message -> onReceive(message) }, Message::class.java)
        hubConnection.on("MessageUpdated", { message -> onUpdate(message) }, Message::class.java)
        hubConnection.on(
            "MessageDeleted",
            { id: String -> onDelete(id) },
            String::class.java
        )
        hubConnection.start().blockingAwait()
        hubConnection.send("JoinChat", chatId)
    }

    fun disconnect() {
        if (::hubConnection.isInitialized) {
            hubConnection.stop()
        }
    }

    suspend fun getMessages(): List<Message> {
        return api.getMessages()
    }

    suspend fun getMessagesFiltered(
        chatId: Int? = null,
        senderId: Int? = null,
        startDate: String? = null,
        endDate: String? = null
    ): List<Message> {
        return api.getMessages(chatId, senderId, startDate, endDate)
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