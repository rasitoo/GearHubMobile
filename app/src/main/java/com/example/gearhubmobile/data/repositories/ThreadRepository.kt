package com.example.gearhubmobile.data.repositories

import com.example.gearhubmobile.data.apirest.ThreadApi
import com.example.gearhubmobile.data.models.CreateThreadRequest
import com.example.gearhubmobile.data.models.Thread
import com.example.gearhubmobile.data.models.UpdateThreadRequest
import retrofit2.Response
import javax.inject.Inject

/**
 * @author Rodrigo
 * @date 29 mayo, 2025
 */
class ThreadRepository @Inject constructor(private val api: ThreadApi) {

    suspend fun getThreadById(threadId: String): Thread {
        return api.getThreadById(threadId)
    }

    suspend fun likeThread(threadId: String): Response<Unit> {
        return api.likeThread(threadId)
    }

    suspend fun createThread(
        title: String,
        content: String,
        communityId: String,
        images: List<String>,
    ): Response<Unit> {
        val threadRequest = CreateThreadRequest(title, content, communityId, images)

        return api.createThread(threadRequest)
    }

    suspend fun unlikeThread(threadId: String): Response<Unit> {
        return api.unlikeThread(threadId)
    }

    suspend fun updateThread(
        id: String,
        title: String,
        content: String,
        images: List<String>,
        imagesToKeep: List<String>
    ): Response<Unit> {
        val threadRequest = UpdateThreadRequest(id, title, content, images, imagesToKeep)

        return api.updateThread(threadRequest)
    }

    suspend fun getAllThreads(): List<Thread> {
        return api.getAllThreads()
    }

    suspend fun getThreadsByCommunity(communityId: String): List<Thread> {
        return api.getThreadsByCommunity(communityId)
    }

    suspend fun getThreadsByCreator(creatorId: String): List<Thread> {
        return api.getThreadsByCreator(creatorId)
    }

    suspend fun getThreadsByLikes(creatorId: String): List<Thread> {
        return api.getThreadsByLikes(creatorId)
    }

    suspend fun hasLikedThread(threadId: String): Boolean {
        return api.hasLikedThread(threadId)
    }

}