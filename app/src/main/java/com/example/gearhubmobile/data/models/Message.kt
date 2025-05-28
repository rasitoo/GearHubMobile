package com.example.gearhubmobile.data.models

import java.time.LocalDateTime

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
data class Message(
    val id: String,
    val chatId: String,
    val senderId: String,
    val content: String?,
    val sentAt: LocalDateTime?
)


data class CreateMessageRequest(
    val content: String,
    val chatId: String
)

data class UpdateMessageRequest(
    val content: String
)