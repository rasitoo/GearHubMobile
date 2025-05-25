package com.example.gearhubmobile.data.models


/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
data class Thread(
    val id: String,
    val title: String,
    val content: String,
    val likes: Int,
    val community: Community,
    val creatorId: String,
    val images: List<String>,
)
