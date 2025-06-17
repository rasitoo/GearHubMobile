package com.example.gearhubmobile.data.apirest

import com.example.gearhubmobile.data.models.CreateUserChatRequest
import com.example.gearhubmobile.data.models.DeleteUserChatRequest
import com.example.gearhubmobile.data.models.UserChatDetailDto
import com.example.gearhubmobile.data.models.UserChatDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @author Rodrigo
 * @date 28 mayo, 2025
 */

interface UserChatApi {
    @GET("api/UserChat")
    suspend fun getUserChats(
        @Query("chatId") chatId: String? = null,
        @Query("chatName") chatName: String? = null
    ): List<UserChatDetailDto>

    @POST("api/UserChat")
    suspend fun createUserChat(@Body request: CreateUserChatRequest): Response<Unit>

    @DELETE("api/UserChat")
    suspend fun deleteUserChat(
        @Query("userId") userId: String,
        @Query("chatId") chatId: String
    ): Response<Unit>}