package com.example.gearhubmobile.data.apirest

/**
 * @author Rodrigo
 * @date 28 mayo, 2025
 */
import com.example.gearhubmobile.data.models.FollowRequest
import com.example.gearhubmobile.data.models.User
import retrofit2.http.*

interface FollowsApi {

    @DELETE("api/Follows/StopFollowing")
    suspend fun stopFollowing(@Query("userId") userId: String, @Query("otherUserId") otherUserId: String)

    @DELETE("api/Follows/DropFollower")
    suspend fun dropFollower(@Query("userId") userId: String, @Query("followerId") followerId: String)

    @GET("api/Follows/IsFollowing/{userId}/{otherUserId}")
    suspend fun isFollowing(
        @Path("userId") userId: String,
        @Path("otherUserId") otherUserId: String
    ): Boolean

    @POST("api/Follows/StartFollowing")
    suspend fun startFollowing(@Body request: FollowRequest)

    @GET("api/Follows/followers/{userId}")
    suspend fun getFollowers(@Path("userId") userId: String): List<User>

    @GET("api/Follows/following/{userId}")
    suspend fun getFollowing(@Path("userId") userId: String): List<User>
}
