package com.example.gearhubmobile.data.apirest

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
import com.example.gearhubmobile.data.models.Community
import retrofit2.http.*

interface CommunityApi {

    @GET("api/Community/All")
    suspend fun getAllCommunities(): List<Community>

    @GET("api/Community/bycreator/{creatorId}")
    suspend fun getCommunitiesByCreator(@Path("creatorId") creatorId: String): List<Community>

    @GET("api/Community/bysubscrition")
    suspend fun getSubscribedCommunities(): List<Community>

    @POST("api/Community")
    suspend fun createCommunity(@Body community: Community): Unit

    @PUT("api/Community")
    suspend fun updateCommunity(@Body community: Community): Unit

    @GET("api/Community/{id}")
    suspend fun getCommunityById(@Path("id") id: String): Community

    @DELETE("api/Community/{id}")
    suspend fun deleteCommunity(@Path("id") id: String): Unit

    @POST("api/Community/SubscribeToCommunity/{comid}")
    suspend fun subscribeToCommunity(@Path("comid") communityId: String): Unit

    @DELETE("api/Community/UnsubscribeToCommunity/{comid}")
    suspend fun unsubscribeFromCommunity(@Path("comid") communityId: String): Unit

    @GET("api/Community/HasSubscription/{communityId}")
    suspend fun hasSubscription(@Path("communityId") communityId: String): Boolean
}
