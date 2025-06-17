package com.example.gearhubmobile.data.models

/**
 * @author Rodrigo
 * @date 28 mayo, 2025
 */
data class UserChatDto(
    val id: String,
    val userId: String,
    val chatId: String,
    val createdAt: String
)
data class UserIdChatResponse(
    val userId: String
)
data class UserChatDetailDto(
    val userId: String,
    val chatId: String,
    val chat: Chat,
    val users: List<UserIdChatResponse>
)

data class CreateUserChatRequest(
    val userId: String,
    val chatId: String
)

data class DeleteUserChatRequest(
    val userId: String,
    val chatId: String
)