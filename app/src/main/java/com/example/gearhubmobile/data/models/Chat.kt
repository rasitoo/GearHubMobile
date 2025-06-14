package com.example.gearhubmobile.data.models


/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
data class Chat(
    val id: String,
    val creatorId: String,
    val name: String?,
    val createdAt: String?
)

data class CreateChatRequest(
    val name: String
)

data class UpdateChatRequest(
    val name: String
)