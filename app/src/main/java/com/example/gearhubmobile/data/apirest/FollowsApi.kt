package com.example.gearhubmobile.data.apirest

/**
 * @author Rodrigo
 * @date 28 mayo, 2025
 */
import com.example.gearhubmobile.data.models.User
import com.example.gearhubmobile.data.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FollowsApi {

    @DELETE("api/Follows/StopFollowing")
    suspend fun stopFollowing(
        @Query("profileId") userId: String): Response<Unit>

    @DELETE("api/Follows/DropFollower")
    suspend fun dropFollower(
        @Query("profileId") userId: String): Response<Unit>

    @GET("api/Follows/IsFollowing/{userId}/{otherUserId}")
    suspend fun isFollowing(
        @Path("userId") userId: String,
        @Path("otherUserId") otherUserId: String
    ): Boolean

    @POST("api/Follows/StartFollowing")
    suspend fun startFollowing(@Query("profileId") profileId: String): Response<Unit>

    @GET("api/Follows/followers/{userId}")
    suspend fun getFollowers(@Path("userId") userId: String): UserResponse

    @GET("api/Follows/following/{userId}")
    suspend fun getFollowing(@Path("userId") userId: String): UserResponse
}
