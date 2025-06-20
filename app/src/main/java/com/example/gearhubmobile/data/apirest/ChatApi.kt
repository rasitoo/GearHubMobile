package com.example.gearhubmobile.data.apirest

import com.example.gearhubmobile.data.models.Chat
import com.example.gearhubmobile.data.models.CreateChatRequest
import com.example.gearhubmobile.data.models.UpdateChatRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
interface ChatApi {

    @GET("api/Chats")
    suspend fun getChats(): List<Chat>

    @POST("api/Chats")
    suspend fun createChat(@Body request: CreateChatRequest): Response<Chat>

    @GET("api/Chats/{id}")
    suspend fun getChatById(@Path("id") id: String): Chat

    @PUT("api/Chats/{id}")
    suspend fun updateChat(@Path("id") id: String, @Body request: UpdateChatRequest): Response<Unit>

    @DELETE("api/Chats/{id}")
    suspend fun deleteChat(@Path("id") id: String): Response<Unit>
}