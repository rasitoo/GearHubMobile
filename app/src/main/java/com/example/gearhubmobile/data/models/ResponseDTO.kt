package com.example.gearhubmobile.data.models


/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
data class ResponseDTO(
    val id: String,
    val creatorId: String,
    val content: String?,
    val likes: Int?,
    val isDeleted: Boolean?,
    val responses: Int?
)
data class CreateResponseRequest(
    val content: String,
    val parentId: String,
    val threadId: String
)

data class UpdateResponseRequest(
    val id: String,
    val content: String
)
