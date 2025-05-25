package com.example.gearhubmobile.data.models


/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
data class Response(
    val id: String,
    val creatorId: String,
    val content: String?,
    val likes: Int?,
    val isDeleted: Boolean?,
    val responses: Int?
)
