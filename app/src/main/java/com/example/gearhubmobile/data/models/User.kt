package com.example.gearhubmobile.data.models


/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
data class User(
    val id: String,
    val profilePicture: String,
    val name: String,
    val userName: String,
    val description: String,
    val address: String,
    val userId: String,
    val countFollowers: Int,
    val countFollowing: Int,
    val type: Int,
)
