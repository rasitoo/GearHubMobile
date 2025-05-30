package com.example.gearhubmobile.data.repositories

import com.example.gearhubmobile.data.apirest.UserChatApi
import com.example.gearhubmobile.data.models.CreateUserChatRequest
import com.example.gearhubmobile.data.models.DeleteUserChatRequest
import com.example.gearhubmobile.data.models.UserChatDto
import retrofit2.Response
import javax.inject.Inject

/**
 * @author Rodrigo
 * @date 29 mayo, 2025
 */
class UserChatRepository @Inject constructor(private val api: UserChatApi) {

    suspend fun getUserChats(threadId: String): List<UserChatDto> {
        return api.getUserChats()
    }

    suspend fun deleteUserChat(userId: String, chatId: String): Response<Unit> {
        val userChatRequest = DeleteUserChatRequest(userId, chatId)

        return api.deleteUserChat(userChatRequest)
    }

    suspend fun createUserChat(userId: String, chatId: String): Response<Unit> {
        val userChatRequest = CreateUserChatRequest(userId, chatId)

        return api.createUserChat(userChatRequest)
    }

}