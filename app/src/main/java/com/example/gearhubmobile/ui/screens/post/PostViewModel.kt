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
import com.example.gearhubmobile.data.models.Community
import com.example.gearhubmobile.data.models.CommunityDto
import com.example.gearhubmobile.data.models.ResponseDTO
import com.example.gearhubmobile.data.models.Thread
import com.example.gearhubmobile.data.models.User
import com.example.gearhubmobile.data.repositories.CommunityRepository
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
    private val responseRepository: ResponseRepository,
    private val communityRepository: CommunityRepository
) : ViewModel() {
    var user by mutableStateOf<User?>(null)
    val threads = mutableStateListOf<Thread>()
    val responsesByThread = mutableStateListOf<ResponseDTO>()
    val communities = MutableStateFlow< List<CommunityDto>>(emptyList())
    var errorMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)

    init {
        viewModelScope.launch {
            communities.value = communityRepository.getAllCommunities()
        }
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