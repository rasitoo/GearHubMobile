package com.example.gearhubmobile.data.apirest

/**
 * @author Rodrigo
 * @date 27 mayo, 2025
 */
import com.example.gearhubmobile.data.models.CreateThreadRequest
import com.example.gearhubmobile.data.models.Thread
import com.example.gearhubmobile.data.models.ThreadResponse
import com.example.gearhubmobile.data.models.UpdateThreadRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ThreadApi {

    @Multipart
    @POST("api/Thread")
    suspend fun createThread(
        @Part("Title") title: RequestBody,
        @Part("Content") content: RequestBody,
        @Part("CommunityId") communityId: RequestBody,
        @Part Images: List<MultipartBody.Part>
    ): Response<Unit>

    @PUT("api/Thread")
    suspend fun updateThread(@Body request: UpdateThreadRequest): Response<Unit>

    @GET("api/Thread/All")
    suspend fun getAllThreads(): ThreadResponse

    @GET("api/Thread/AllByComunity/{idCom}")
    suspend fun getThreadsByCommunity(@Path("idCom") communityId: String): ThreadResponse

    @GET("api/Thread/HasLike/{threadId}")
    suspend fun hasLikedThread(@Path("threadId") threadId: String): Boolean

    @POST("api/Thread/LikeThread/{threadId}")
    suspend fun likeThread(@Path("threadId") threadId: String): Response<Unit>

    @DELETE("api/Thread/UnlikeThread/{threadId}")
    suspend fun unlikeThread(@Path("threadId") threadId: String): Response<Unit>

    @GET("api/Thread/bylike/{creatorId}")
    suspend fun getThreadsByLikes(@Path("creatorId") creatorId: String): ThreadResponse

    @GET("api/Thread/bycreator/{creatorId}")
    suspend fun getThreadsByCreator(@Path("creatorId") creatorId: String): ThreadResponse

    @GET("api/Thread/{idThread}")
    suspend fun getThreadById(@Path("idThread") threadId: String): Thread
}
