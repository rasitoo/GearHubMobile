package com.example.gearhubmobile.data.repositories

import android.util.Log
import com.example.gearhubmobile.data.apirest.UserChatApi
import com.example.gearhubmobile.data.models.CreateUserChatRequest
import com.example.gearhubmobile.data.models.DeleteUserChatRequest
import com.example.gearhubmobile.data.models.User
import com.example.gearhubmobile.data.models.UserChatDetailDto
import com.example.gearhubmobile.data.models.UserChatDto
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.TransportEnum
import retrofit2.Response
import javax.inject.Inject

/**
 * @author Rodrigo
 * @date 29 mayo, 2025
 */
class UserChatRepository @Inject constructor(private val api: UserChatApi) {
    private lateinit var hubConnection: HubConnection

    fun connect(
        onUserJoined: (String) -> Unit,
        onUserLeft: (String) -> Unit,
        onOwnConnectionId: (String) -> Unit
    ) {
        hubConnection = HubConnectionBuilder
            .create("http://vms.iesluisvives.org:25003/hubs/messages")
//            .withTransport(TransportEnum.WEBSOCKETS).shouldSkipNegotiate(true)
////            .skipNegotiate()
            .build()

        hubConnection.on("UserJoined", { args ->
            println("RAW ARGS for UserJoined: ${args.toString()}")
            println("ARG TYPE: ${args::class.qualifiedName}")
        }, Any::class.java)
        hubConnection.on("UserLeft", { message ->
            onUserLeft(message)
        }, String::class.java)

        hubConnection.start().doOnComplete {
            hubConnection.connectionId?.let { onOwnConnectionId(it) }
        }.blockingAwait()
        Log.d("SIGNALR", "Connection started. ID: ${hubConnection.connectionId}")

    }


    suspend fun getUserChats(chatId: String): UserChatDetailDto {
        return api.getUserChats(chatId = chatId).get(0)
    }

    suspend fun deleteUserChat(userId: String, chatId: String): Response<Unit> {
        val userChatRequest = DeleteUserChatRequest(userId, chatId)

        return api.deleteUserChat(chatId = userChatRequest.chatId.toString(),userId = userChatRequest.userId.toString())
    }

    suspend fun createUserChat(userId: String, chatId: String): Response<Unit> {
        val userChatRequest = CreateUserChatRequest(userId, chatId)

        return api.createUserChat(userChatRequest)
    }

}