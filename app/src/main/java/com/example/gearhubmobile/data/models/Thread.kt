package com.example.gearhubmobile.data.models


/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
data class Thread(
    val id: String,
    val title: String,
    val content: String,
    var likes: Int?,
    val community: Community,
    val creatorId: String,
    val images: List<String>
)
data class ThreadResponse(
    val data: List<Thread>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalRecords: Int,
    val totalPages: Int
)

data class CreateThreadRequest(
    val title: String,
    val content: String,
    val communityId: String,
    val images: List<String>
)

data class UpdateThreadRequest(
    val id: String,
    val title: String,
    val content: String,
    val images: List<String>,
    val imagesToKeep: List<String>

)
