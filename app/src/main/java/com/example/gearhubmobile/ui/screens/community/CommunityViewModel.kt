package com.example.gearhubmobile.ui.screens.community

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gearhubmobile.data.models.Community
import com.example.gearhubmobile.data.models.Thread
import com.example.gearhubmobile.data.repositories.CommunityRepository
import com.example.gearhubmobile.data.repositories.ThreadRepository
import com.example.gearhubmobile.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val repository: CommunityRepository,
    private val threadRepository: ThreadRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    var communities by mutableStateOf<List<Community>>(emptyList())
    var myCommunities by mutableStateOf<List<Community>>(emptyList())
    var community = mutableStateOf<Community?>(null)
    var communityPosts = mutableStateOf<List<Thread>>(emptyList())
    val likesState = mutableStateMapOf<String, Boolean>()

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var comCreated by mutableStateOf(false)


    var currentIsWorkshop by mutableStateOf(false)
    var currentId by mutableStateOf<String?>(null)

    suspend fun getCurrentData() {
        currentIsWorkshop = sessionManager.getUserType() == 2
        currentId = sessionManager.getUserId()
    }

    fun loadCommunities() {
        viewModelScope.launch {
            isLoading = true
            try {
                val temp = repository.getAllCommunities()
                myCommunities = emptyList()
                communities = emptyList()
                temp.forEach { community ->
                    val com = repository.getCommunityById(id = community.id)
                    if (com.creatorId == currentId)
                        myCommunities = myCommunities + com
                    else
                        communities = communities + com
                }
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }


    fun loadCommunity(id: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                community.value = repository.getCommunityById(id)
            } catch (_: Exception) {
            }
            isLoading = false
        }
    }

    fun createCommunity(
        ComName: String,
        ComDesc: String,
        profileImage: Uri?,
        bannerImage: Uri?,
        context: Context
    ) {
        errorMessage = when {
            ComName.isBlank() -> "Por favor, introduce el nombre de la comunidad."
            ComDesc.isBlank() -> "Por favor, introduce la descripcion de la comunidad."
            else -> null
        }
        if (errorMessage != null) return

        val profileImagePart = createImagePart(context, profileImage, "profileImage")
        val bannerImagePart = createImagePart(context, bannerImage, "bannerImage")

        viewModelScope.launch {
            val result = repository.createCommunity(
                ComName.toRequestBody(),
                ComDesc.toRequestBody(),
                profileImagePart,
                bannerImagePart,
            )
            comCreated = result.isSuccessful
            if (!result.isSuccessful) {
                errorMessage = result.message() ?: "Error al crear la comunidad"
            }else{
                repository.subscribeToCommunity(result.body()?.id ?: "-1")
            }
        }
    }

    fun createImagePart(context: Context, imageUri: Uri?, partName: String): MultipartBody.Part? {
        if (imageUri == null) return null
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(imageUri) ?: "image/*"
        val fileName = getFileNameFromUri(context, imageUri)
        val inputStream = contentResolver.openInputStream(imageUri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        val requestFile = bytes?.toRequestBody(mimeType.toMediaTypeOrNull())
        return requestFile?.let {
            MultipartBody.Part.createFormData(partName, fileName, it)
        }
    }

    fun getFileNameFromUri(context: Context, uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (index >= 0) result = it.getString(index)
                }
            }
        }
        if (result == null) {
            result = uri.lastPathSegment?.substringAfterLast('/')
        }
        return result ?: (System.currentTimeMillis().toString() + ".png")
    }

    fun loadPosts(communityId: String) {
        viewModelScope.launch {
            try {
                val posts = threadRepository.getAllThreads()
                communityPosts.value = posts
                likesState.clear()
                posts.forEach { post ->
                    val liked = threadRepository.hasLikedThread(post.id)
                    likesState[post.id] = liked
                }
            } catch (_: Exception) {
            }
        }
    }

    fun loadAllPosts() {
        viewModelScope.launch {
            try {
                val posts = threadRepository.getAllThreads()
                communityPosts.value = posts

                likesState.clear()
                posts.forEach { post ->
                    val liked = threadRepository.hasLikedThread(post.id)
                    likesState[post.id] = liked
                }
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }



    fun toggleLike(id: String) {
        viewModelScope.launch {
            val current = likesState[id] == true
            try {
                val success = if (current)
                    threadRepository.unlikeThread(id).isSuccessful
                else
                    threadRepository.likeThread(id).isSuccessful

                if (success) {
                    likesState[id] = !current
                }
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

}
