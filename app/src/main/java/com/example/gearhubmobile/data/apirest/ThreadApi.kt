package com.example.gearhubmobile.data.apirest

/**
 * @author Rodrigo
 * @date 27 mayo, 2025
 */
import com.example.gearhubmobile.data.models.CreateThreadRequest
import com.example.gearhubmobile.data.models.UpdateThreadRequest
import retrofit2.Response
import retrofit2.http.*

interface ThreadApi {

    @POST("api/Thread")
    suspend fun createThread(@Body request: CreateThreadRequest) : Response<Unit>

    @PUT("api/Thread")
    suspend fun updateThread(@Body request: UpdateThreadRequest) : Response<Unit>

    @GET("api/Thread/All")
    suspend fun getAllThreads(): List<Thread>

    @GET("api/Thread/AllByComunity/{idCom}")
    suspend fun getThreadsByCommunity(@Path("idCom") communityId: String): List<Thread>

    @GET("api/Thread/HasLike/{threadId}")
    suspend fun hasLikedThread(@Path("threadId") threadId: String): Boolean

    @POST("api/Thread/LikeThread/{threadId}")
    suspend fun likeThread(@Path("threadId") threadId: String) : Response<Unit>

    @DELETE("api/Thread/UnlikeThread/{threadId}")
    suspend fun unlikeThread(@Path("threadId") threadId: String) : Response<Unit>

    @GET("api/Thread/bylike/{creatorId}")
    suspend fun getThreadsByLikes(@Path("creatorId") creatorId: String): List<Thread>

    @GET("api/Thread/bycreator/{creatorId}")
    suspend fun getThreadsByCreator(@Path("creatorId") creatorId: String): List<Thread>

    @GET("api/Thread/{idThread}")
    suspend fun getThreadById(@Path("idThread") threadId: String): Thread
}
