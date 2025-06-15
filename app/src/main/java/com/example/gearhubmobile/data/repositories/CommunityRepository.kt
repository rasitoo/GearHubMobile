package com.example.gearhubmobile.data.repositories

import com.example.gearhubmobile.data.apirest.CommunityApi
import com.example.gearhubmobile.data.models.Community
import com.example.gearhubmobile.data.models.CommunityDto
import com.example.gearhubmobile.data.models.CommunityUpdateDTO
import com.example.gearhubmobile.data.models.CreateCommunityResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
class CommunityRepository @Inject constructor(private val api: CommunityApi) {

    suspend fun getAllCommunities(): List<CommunityDto> {
        return api.getAllCommunities().data
    }

    suspend fun getCommunityById(id: String): Community {
        return api.getCommunityById(id)
    }

    suspend fun createCommunity(
        comName: RequestBody,
        comDesc: RequestBody,
        profileImg: MultipartBody.Part?,
        bannerImg: MultipartBody.Part?
    ): Response<CreateCommunityResponse> {
        return api.createCommunity(comName, comDesc, profileImg, bannerImg)

    }

    suspend fun getCommunitiesByCreator(creatorID: String): List<Community> {
        return api.getCommunitiesByCreator(creatorID)
    }

    suspend fun getSubscribedCommunities(): List<Community> {
        return api.getSubscribedCommunities()
    }

    suspend fun deleteCommunity(id: String): Response<Unit> {
        return api.deleteCommunity(id)
    }

    suspend fun hasSubscription(id: String): Boolean {
        return api.hasSubscription(id)
    }

    suspend fun subscribeToCommunity(id: String): Response<Unit> {
        return api.subscribeToCommunity(id)
    }

    suspend fun unsubscribeFromCommunity(id: String): Response<Unit> {
        return api.unsubscribeFromCommunity(id)
    }

    suspend fun updateCommunity(
        id: String,
        comName: String,
        comDesc: String,
        profileImg: String,
        bannerImg: String
    ): Response<Unit> {
        val communityRequest = CommunityUpdateDTO(id, comName, comDesc, profileImg, bannerImg)
        return api.updateCommunity(communityRequest)
    }

}