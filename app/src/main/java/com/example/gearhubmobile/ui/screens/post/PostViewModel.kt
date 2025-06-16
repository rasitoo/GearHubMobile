package com.example.gearhubmobile.ui.screens.post

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gearhubmobile.data.models.CommunityDto
import com.example.gearhubmobile.data.models.ResponseDTO
import com.example.gearhubmobile.data.models.Thread
import com.example.gearhubmobile.data.models.User
import com.example.gearhubmobile.data.repositories.CommunityRepository
import com.example.gearhubmobile.data.repositories.ProfileRepository
import com.example.gearhubmobile.data.repositories.ResponseRepository
import com.example.gearhubmobile.data.repositories.ThreadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
class PostViewModel @Inject constructor(
    private val threadRepository: ThreadRepository,
    private val profileRepository: ProfileRepository,
    private val responseRepository: ResponseRepository,
    private val communityRepository: CommunityRepository
) : ViewModel() {
    var user by mutableStateOf<User?>(null)
    val threads = mutableStateListOf<Thread>()
    val communities = MutableStateFlow<List<CommunityDto>>(emptyList())
    var errorMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)
    val responsesByThread = mutableStateOf<Map<String, List<ResponseDTO>>>(emptyMap())
    val responsesByResponse = mutableStateOf<Map<String, List<ResponseDTO>>>(emptyMap())
    var likesState by mutableStateOf<Map<String, Boolean>>(emptyMap())
    var responsesUsers by mutableStateOf<Map<String, User>>(emptyMap())

    var selectedCommunity by mutableStateOf<CommunityDto?>(null)



    fun getUser(id: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                user = profileRepository.getUserById(id).getOrNull()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun toggleResponseLike(id: String) {
        viewModelScope.launch {
            val current = likesState[id] == true
            try {
                if (current) responseRepository.unlikeResponse(id).isSuccessful
                else responseRepository.likeResponse(id).isSuccessful

                likesState = likesState.toMutableMap().apply {
                    this[id] = !current
                }
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

    var thread by mutableStateOf<Thread?>(null)


    init {
        viewModelScope.launch {
            communities.value = communityRepository.getAllCommunities()
        }
    }

    fun setResponsesForThread(threadId: String, responses: List<ResponseDTO>) {
        responsesByThread.value = responsesByThread.value.toMutableMap().apply {
            put(threadId, responses)
        }
    }

    fun getResponsesForThread(threadId: String): List<ResponseDTO> {
        return responsesByThread.value[threadId] ?: emptyList()
    }

    fun setResponsesForResponse(threadId: String, responses: List<ResponseDTO>) {
        responsesByResponse.value = responsesByResponse.value.toMutableMap().apply {
            put(threadId, responses)
        }
    }

    fun getResponsesForResponse(threadId: String): List<ResponseDTO> {
        return responsesByResponse.value[threadId] ?: emptyList()
    }

fun loadThread(threadId: String) {
    viewModelScope.launch {
        thread = threadRepository.getThreadById(threadId)
        thread?.let { t ->
            val mainResponses = responseRepository.getResponsesByThread(t.id.toString())
            setResponsesForThread(t.id, mainResponses)
            updateUsersForResponses(mainResponses)

            suspend fun loadSubResponsesRecursively(response: ResponseDTO) {
                val subResponses = responseRepository.getResponsesByResponse(threadId, response.id)
                setResponsesForResponse(response.id, subResponses)
                updateUsersForResponses(subResponses)
                for (sub in subResponses) {
                    loadSubResponsesRecursively(sub)
                }
            }
            for (response in mainResponses) {
                loadSubResponsesRecursively(response)
            }
        }
    }
}

private suspend fun updateUsersForResponses(responses: List<ResponseDTO>) {
    val missingUserIds = responses.map { it.creatorId }
        .filter { !responsesUsers.containsKey(it) }
        .distinct()

    val newUsers = missingUserIds.mapNotNull { userId ->
        profileRepository.getUserById(userId).getOrNull()?.let { user ->
            userId to user
        }
    }.toMap()
    responsesUsers = responsesUsers + newUsers
}
    fun createPost(
        title: String,
        content: String,
        communityId: String,
        imageUris: List<Uri>,
        context: Context
    ) {
        val contentResolver = context.contentResolver
        val imageParts = imageUris.mapNotNull { uri ->
            val mimeType = contentResolver.getType(uri) ?: "image/*"
            val fileName = getFileNameFromUri(context, uri)
            val inputStream = contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            inputStream?.close()
            val requestFile = bytes?.toRequestBody(mimeType.toMediaTypeOrNull())
            requestFile?.let {
                MultipartBody.Part.createFormData("Images", fileName, it)
            }
        }

        viewModelScope.launch {
            threadRepository.createThread(
                title.toRequestBody(),
                content.toRequestBody(),
                communityId.toRequestBody(),
                imageParts
            )
        }
    }

    fun createResponse(
        content: String,
        threadId: String,
        parentId: String
    ) {

        viewModelScope.launch {
            responseRepository.createResponse(
                content = content, threadId = threadId, parentId = parentId
            )
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
}