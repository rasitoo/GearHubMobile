package com.example.gearhubmobile.data.repositories

import com.example.gearhubmobile.data.apirest.ProfileApi
import com.example.gearhubmobile.data.models.User
import com.example.gearhubmobile.data.models.UserProfileCreateRequest
import com.example.gearhubmobile.data.models.UserProfileUpdateRequest
import retrofit2.Response

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
class ProfileRepository(private val api: ProfileApi) {

    suspend fun getAllUsers(): List<User> {
        return api.getAllUsers()
    }

    suspend fun getUserById(id: String): User {
        return api.getUserById(id)
    }

    suspend fun createUserProfile(
        name: String,
        userName: String,
        desc: String,
        address: String,
        profilePicture: String,
    ): Response<Unit> {
        val profileRequest = UserProfileCreateRequest(name, userName, desc, address, profilePicture)

        return api.createUserProfile(profileRequest)
    }

    suspend fun updateUserProfile(
        name: String,
        userName: String,
        desc: String,
        address: String,
        profilePicture: String,
    ): Response<Unit> {
        val profileRequest = UserProfileUpdateRequest(name, userName, desc, address, profilePicture)

        return api.updateUserProfile(profileRequest)
    }

    suspend fun getWorkshopData(): User {
        return api.getWorkshopData()
    }

}