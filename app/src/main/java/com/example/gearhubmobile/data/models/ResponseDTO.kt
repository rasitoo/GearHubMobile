package com.example.gearhubmobile.data.models


/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
data class ResponseDTO(
    val id: String,
    val creatorId: String,
    val content: String?,
    var likes: Int?,
    val isDeleted: Boolean?,
    val responses: Int?
)

data class ResponseRequest(
    val data: List<ResponseDTO>,
    val pageNumber: Int?,
    val pageSize: Int?,
    val totalRecords: Int?,
    val totalPages: Int?
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
