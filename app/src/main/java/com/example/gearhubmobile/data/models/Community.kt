package com.example.gearhubmobile.data.models


/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
data class Community(
    val id: String,
    val name: String,
    val picture: String?,
    val banner: String?,
    val description: String?,
    val creatorId: String,
    val subscriptions: Int?
)
