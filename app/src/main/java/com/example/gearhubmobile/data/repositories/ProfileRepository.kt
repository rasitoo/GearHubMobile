package com.example.gearhubmobile.data.repositories

import com.example.gearhubmobile.data.apirest.ProfileApi
import com.example.gearhubmobile.data.models.User
import com.example.gearhubmobile.data.models.UserProfileUpdateRequest
import com.example.gearhubmobile.data.models.UserReduction
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
class ProfileRepository @Inject constructor(private val api: ProfileApi) {

    suspend fun getAllUsers(): List<UserReduction> {
        return api.getAllUsers().data
    }

    suspend fun createUserProfile(
        name: RequestBody,
        username: RequestBody,
        description: RequestBody,
        address: RequestBody,
        profilePictureUri: MultipartBody.Part?
    ) : Response<Unit>{


        return api.createCommunity(
            name = name,
            username = username,
            description = description,
            address = address,
            profilePicture = profilePictureUri
        )
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

    suspend fun getUserById(id: String): Result<User?> {
        return try {
            val response = api.getUserById(id)
            when (response.code()) {
                200 -> Result.success(response.body())
                401 -> Result.failure(Exception("UNAUTHORIZED"))
                404 -> Result.failure(Exception("NOT_FOUND"))
                else -> Result.failure(Exception("UNKNOWN"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}