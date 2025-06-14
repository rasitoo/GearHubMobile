package com.example.gearhubmobile.data.apirest

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
import com.example.gearhubmobile.data.models.User
import com.example.gearhubmobile.data.models.UserProfileUpdateRequest
import com.example.gearhubmobile.data.models.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ProfileApi {

    @Multipart
    @POST("api/UserProfile/UserCreate")
    suspend fun createUser(
        @Part("Name") name: RequestBody,
        @Part("UserName") username: RequestBody,
        @Part("Description") description: RequestBody,
        @Part("Address") address: RequestBody,
        @Part profilePicture: MultipartBody.Part?
    ): Response<Unit>


    @PATCH("api/UserProfile/UserUpdate")
    suspend fun updateUserProfile(@Body profile: UserProfileUpdateRequest) : Response<Unit>

    @GET("api/UserProfile/Users")
    suspend fun getAllUsers(): UserResponse

    @GET("api/UserProfile/Users/GetUserById/{userId}")
    suspend fun getUserById(@Path("userId") userId: String): Response<User>

    @GET("api/UserProfile/workshop")
    suspend fun getWorkshopData(): User
}
