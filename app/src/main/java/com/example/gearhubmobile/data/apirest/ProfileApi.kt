package com.example.gearhubmobile.data.apirest

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
import com.example.gearhubmobile.data.models.User
import com.example.gearhubmobile.data.models.UserProfileCreateRequest
import com.example.gearhubmobile.data.models.UserProfileUpdateRequest
import retrofit2.http.*

interface ProfileApi {

    @POST("api/UserProfile/UserCreate")
    suspend fun createUserProfile(@Body profile: UserProfileCreateRequest)

    @PATCH("api/UserProfile/UserUpdate")
    suspend fun updateUserProfile(@Body profile: UserProfileUpdateRequest)

    @GET("api/UserProfile/Users")
    suspend fun getAllUsers(): List<User>

    @GET("api/UserProfile/Users/GetUserById/{userId}")
    suspend fun getUserById(@Path("userId") userId: String): User

    @GET("api/UserProfile/workshop")
    suspend fun getWorkshopData(): User
}
