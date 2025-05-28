package com.example.gearhubmobile.data.apirest

import com.example.gearhubmobile.data.models.Chat
import com.example.gearhubmobile.data.models.CreateChatRequest
import com.example.gearhubmobile.data.models.UpdateChatRequest
import retrofit2.http.*

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
interface ChatApi {

    @GET("api/Chats")
    suspend fun getChats(): List<Chat>

    @POST("api/Chats")
    suspend fun createChat(@Body request: CreateChatRequest)

    @GET("api/Chats/{id}")
    suspend fun getChatById(@Path("id") id: String): Chat

    @PUT("api/Chats/{id}")
    suspend fun updateChat(@Path("id") id: String, @Body request: UpdateChatRequest)

    @DELETE("api/Chats/{id}")
    suspend fun deleteChat(@Path("id") id: String)
}