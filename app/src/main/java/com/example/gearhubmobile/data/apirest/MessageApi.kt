package com.example.gearhubmobile.data.apirest

import com.example.gearhubmobile.data.models.CreateMessageRequest
import com.example.gearhubmobile.data.models.Message
import com.example.gearhubmobile.data.models.UpdateMessageRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author Rodrigo
 * @date 28 mayo, 2025
 */
interface MessageApi {

    @GET("api/Messages/{id}")
    suspend fun getMessageById(@Path("id") id: String): Message

    @PUT("api/Messages/{id}")
    suspend fun updateMessage(
        @Path("id") id: String,
        @Body request: UpdateMessageRequest
    ): Response<Unit>

    @DELETE("api/Messages/{id}")
    suspend fun deleteMessage(@Path("id") id: String): Response<Unit>

    @GET("api/messages")
    suspend fun getMessages(
        @Query("chatId") chatId: Int? = null,
        @Query("senderId") senderId: Int? = null,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
    ): List<Message>

    @POST("api/messages")
    suspend fun createMessage(@Body request: CreateMessageRequest): Response<Unit>
}