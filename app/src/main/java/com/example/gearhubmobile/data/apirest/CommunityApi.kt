package com.example.gearhubmobile.data.apirest

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
import com.example.gearhubmobile.data.models.Community
import com.example.gearhubmobile.data.models.CommunityResponse
import com.example.gearhubmobile.data.models.CommunityUpdateDTO
import com.example.gearhubmobile.data.models.CreateCommunityResponse
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

interface CommunityApi {

    @GET("api/Community/All")
    suspend fun getAllCommunities(): CommunityResponse

    @GET("api/Community/bycreator/{creatorId}")
    suspend fun getCommunitiesByCreator(@Path("creatorId") creatorId: String): List<Community>

    @GET("api/Community/bysubscrition")
    suspend fun getSubscribedCommunities(): List<Community>

    @Multipart
    @POST("api/Community")
    suspend fun createCommunity(
        @Part("ComName") name: RequestBody,
        @Part("ComDescription") username: RequestBody,
        @Part profileImage: MultipartBody.Part?,
        @Part bannerImage: MultipartBody.Part?
    ): Response<CreateCommunityResponse>


    @PUT("api/Community")
    suspend fun updateCommunity(@Body community: CommunityUpdateDTO): Response<Unit>

    @GET("api/Community/{id}")
    suspend fun getCommunityById(@Path("id") id: String): Community

    @DELETE("api/Community/{id}")
    suspend fun deleteCommunity(@Path("id") id: String): Response<Unit>

    @POST("api/Community/SubscribeToCommunity/{comid}")
    suspend fun subscribeToCommunity(@Path("comid") communityId: String): Response<Unit>

    @DELETE("api/Community/UnsubscribeToCommunity/{comid}")
    suspend fun unsubscribeFromCommunity(@Path("comid") communityId: String): Response<Unit>

    @GET("api/Community/HasSubscription/{communityId}")
    suspend fun hasSubscription(@Path("communityId") communityId: String): Boolean
}
