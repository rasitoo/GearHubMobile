package com.example.gearhubmobile.data.repositories

import com.example.gearhubmobile.data.apirest.ChatApi
import com.example.gearhubmobile.data.models.Chat
import com.example.gearhubmobile.data.models.CreateChatRequest
import com.example.gearhubmobile.data.models.UpdateChatRequest
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.TransportEnum
import retrofit2.Response
import javax.inject.Inject

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
class ChatRepository @Inject constructor(private val api: ChatApi) {
    private lateinit var hubConnection: HubConnection


    fun connect(
        onChatDeleted: (String) -> Unit,
        onChatUpdated: (Chat) -> Unit
    ) {
        hubConnection =
            HubConnectionBuilder.create("http://vms.iesluisvives.org:25003/hubs/messages")
                .withTransport(TransportEnum.WEBSOCKETS)
                .build()
        hubConnection.on("ChatDeleted", { id -> onChatDeleted(id) }, String::class.java)
        hubConnection.on("ChatUpdated", { chat -> onChatUpdated(chat) }, Chat::class.java)
        hubConnection.start().blockingAwait()
    }
    suspend fun getChats(): List<Chat> {
        return api.getChats()
    }

    suspend fun getChatById(id: String): Chat {
        return api.getChatById(id)
    }

    suspend fun createChat(name: String): Response<Chat> {
        val chatRequest = CreateChatRequest(name)
        return api.createChat(chatRequest)
    }

    suspend fun deleteChat(id: String): Response<Unit> {
        return api.deleteChat(id)
    }

    suspend fun updateChat(id: String, name: String): Response<Unit> {
        val chatRequest = UpdateChatRequest(name)
        return api.updateChat(id, chatRequest)
    }
}
