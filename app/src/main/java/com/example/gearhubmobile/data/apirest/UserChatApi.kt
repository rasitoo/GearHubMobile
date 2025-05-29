package com.example.gearhubmobile.data.apirest

import com.example.gearhubmobile.data.models.CreateUserChatRequest
import com.example.gearhubmobile.data.models.DeleteUserChatRequest
import com.example.gearhubmobile.data.models.UserChatDto
import retrofit2.Response
import retrofit2.http.*

/**
 * @author Rodrigo
 * @date 28 mayo, 2025
 */

interface UserChatApi {

    @GET("api/UserChat")
    suspend fun getUserChats(): List<UserChatDto>

    @POST("api/UserChat")
    suspend fun createUserChat(@Body request: CreateUserChatRequest) : Response<Unit>

    @DELETE("api/UserChat")
    suspend fun deleteUserChat(@Body request: DeleteUserChatRequest) : Response<Unit>
}