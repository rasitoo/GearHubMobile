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

data class UserProfileCreateRequest(
    val name: String,
    val userName: String,
    val desc: String,
    val address: String? = null,
    val profilePicture: String? = null
)

data class UserProfileUpdateRequest(
    val name: String,
    val userName: String,
    val desc: String?,
    val address: String?,
    val profilePicture: String?
)

data class FollowRequest(
    val userId: String,
    val otherUserId: String
)
