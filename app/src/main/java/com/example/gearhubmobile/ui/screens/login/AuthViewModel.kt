package com.example.gearhubmobile.ui.screens.login

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gearhubmobile.data.models.LoginResponse
import com.example.gearhubmobile.data.repositories.AuthRepository
import com.example.gearhubmobile.data.repositories.ProfileRepository
import com.example.gearhubmobile.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val userRepository: ProfileRepository,
    internal val sessionManager: SessionManager
) : ViewModel() {
    var name = ""
    val token = sessionManager.token
    var userCreated by mutableStateOf(false)
    var loginResult by mutableStateOf<Result<LoginResponse>?>(null)
    var imageError by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            loginResult = repository.login(email, password)
            isLoading = false
        }
    }

    fun clearToken() {
        viewModelScope.launch {
            sessionManager.clearToken()
        }
    }

    fun register(
        name: String,
        password: String,
        passwordRepeat: String,
        email: String,
        isWorkshop: Boolean
    ) {
        viewModelScope.launch {
            repository.register(name, password, passwordRepeat, email, if (isWorkshop) 2 else 1)
        }
    }

    fun recover(email: String) {
        viewModelScope.launch {
            repository.recover(email)
        }
    }
    suspend fun logout() {
        sessionManager.clearToken()
    }

    suspend fun checkUserStatus(): String {
        val userId = sessionManager.getUserId()
        if (userId.isNullOrBlank()) return "UNAUTHORIZED"

        val result = userRepository.getUserById(userId)
        return when (result.exceptionOrNull()?.message) {
            null -> "OK"
            "NOT_FOUND" -> "NOT_FOUND"
            "UNAUTHORIZED" -> "UNAUTHORIZED"
            else -> "UNKNOWN"
        }
    }

    fun createUser(
        name: String,
        username: String,
        description: String,
        address: String,
        profilePictureUri: Uri?,
        context: Context
    ) {
        imageError = when {
            name.isBlank() -> "Por favor, introduce tu nombre."
            username.isBlank() -> "Por favor, introduce un nombre de usuario."
            description.isBlank() -> "Por favor, introduce una descripción."
            address.isBlank() -> "Por favor, introduce una dirección."
            profilePictureUri == null -> "Por favor, añade una imagen de perfil."
            else -> null
        }
        if (imageError != null) return

        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(profilePictureUri!!) ?: "image/*"
        val fileName = getFileNameFromUri(context, profilePictureUri)
        val inputStream = contentResolver.openInputStream(profilePictureUri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        val requestFile = bytes?.toRequestBody(mimeType.toMediaTypeOrNull())
        val picturePart = requestFile?.let {
            MultipartBody.Part.createFormData("profilePicture", fileName, it)
        }

        viewModelScope.launch {
            val result = userRepository.createUserProfile(
                name.toRequestBody(),
                username.toRequestBody(),
                description.toRequestBody(),
                address.toRequestBody(),
                picturePart
            )
            userCreated = result.isSuccessful
            if (!result.isSuccessful) {
                imageError = result.message() ?: "Error al crear usuario"
            }
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