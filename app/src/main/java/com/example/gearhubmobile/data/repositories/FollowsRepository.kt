package com.example.gearhubmobile.data.repositories

import com.example.gearhubmobile.data.apirest.FollowsApi
import com.example.gearhubmobile.data.models.User
import retrofit2.Response
import javax.inject.Inject

/**
 * @author Rodrigo
 * @date 29 mayo, 2025
 */
class FollowsRepository @Inject constructor(private val api: FollowsApi) {

    suspend fun getFollowers(id: String): List<User> {
        return api.getFollowers(id)
    }

    suspend fun getFollowing(id: String): List<User> {
        return api.getFollowing(id)
    }

    suspend fun isFollowing(id: String, otherUserId: String): Boolean {
        return api.isFollowing(id, otherUserId)
    }

    suspend fun startFollowing(id: String): Response<Unit> {
        return api.startFollowing(id)
    }

    suspend fun dropFollower(id: String, followerid: String): Response<Unit> {
        return api.dropFollower(id, followerid)
    }

    suspend fun stopFollowing(id: String, followingid: String): Response<Unit> {
        return api.stopFollowing(id, followingid)
    }

}