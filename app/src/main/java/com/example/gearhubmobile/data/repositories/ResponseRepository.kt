package com.example.gearhubmobile.data.repositories

import com.example.gearhubmobile.data.apirest.ResponseApi
import com.example.gearhubmobile.data.models.CreateResponseRequest
import com.example.gearhubmobile.data.models.ResponseDTO
import com.example.gearhubmobile.data.models.UpdateResponseRequest
import retrofit2.Response
import javax.inject.Inject

/**
 * @author Rodrigo
 * @date 29 mayo, 2025
 */
class ResponseRepository @Inject constructor(private val api: ResponseApi) {

    suspend fun getResponseById(id: String): ResponseDTO {
        return api.getResponseById(id)
    }

    suspend fun getResponsesByThread(id: String): List<ResponseDTO> {
        return api.getResponsesByThread(id).data
    }
    suspend fun getResponsesByResponse(threadId: String,responseId: String): List<ResponseDTO> {
        return api.getResponsesByResponse(threadId,responseId).data
    }

    suspend fun getResponsesByLikes(id: String): List<ResponseDTO> {
        return api.getResponsesByLikes(id).data
    }

    suspend fun getResponsesByCreator(id: String): List<ResponseDTO> {
        return api.getResponsesByCreator(id).data
    }

    suspend fun createResponse(
        content: String,
        parentId: String,
        threadId: String,
    ): Response<Unit> {
        val responseRequest = CreateResponseRequest(content = content, parentId = parentId, threadId = threadId)

        return api.createResponse(responseRequest)
    }

    suspend fun hasLike(id: String): Boolean {
        return api.hasLike(id)
    }

    suspend fun likeResponse(id: String): Response<Unit> {
        return api.likeResponse(id)
    }

    suspend fun unlikeResponse(id: String): Response<Unit> {
        return api.unlikeResponse(id)
    }

    suspend fun updateResponse(id: String, content: String): Response<Unit> {
        val responseRequest = UpdateResponseRequest(id, content)

        return api.updateResponse(responseRequest)
    }


}