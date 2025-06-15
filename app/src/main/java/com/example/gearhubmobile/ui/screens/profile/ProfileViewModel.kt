package com.example.gearhubmobile.ui.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gearhubmobile.data.models.ResponseDTO
import com.example.gearhubmobile.data.models.Thread
import com.example.gearhubmobile.data.models.User
import com.example.gearhubmobile.data.repositories.ProfileRepository
import com.example.gearhubmobile.data.repositories.ResponseRepository
import com.example.gearhubmobile.data.repositories.ThreadRepository
import com.example.gearhubmobile.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val threadRepository: ThreadRepository,
    private val responseRepository: ResponseRepository,
    private val sessionManager: SessionManager
) :
    ViewModel() {
    var responsesUsers by mutableStateOf<Map<String, User>>(emptyMap())
    var threads by mutableStateOf<List<Thread>?>(emptyList())
    var responses by mutableStateOf<List<ResponseDTO>?>(emptyList())
    var users by mutableStateOf<List<User>?>(emptyList())
    var user by mutableStateOf<User?>(null)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var likesState by mutableStateOf<Map<String, Boolean>>(emptyMap())

    fun getUser(id: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                user = repository.getUserById(id).getOrNull()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun getTokenUser() {
        viewModelScope.launch {
            isLoading = true
            try {
                user = repository.getUserById(sessionManager.getUserId().toString()).getOrNull()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun getPostsByLikes(id: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                threads = threadRepository.getThreadsByLikes(id).toList()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }
    fun getUsers() {
        viewModelScope.launch {
            isLoading = true
            try {
                val userList = repository.getAllUsers().mapNotNull { user ->
                    repository.getUserById(user.userId).getOrNull()
                }
                users = userList
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun getPostsByCreator(id: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                threads = threadRepository.getThreadsByCreator(id).toList()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun getResponsesByLikes(id: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                responses = responseRepository.getResponsesByLikes(id).toList()
                responsesUsers = responses?.mapNotNull { response ->
                    repository.getUserById(response.creatorId).getOrNull()?.let { user ->
                        response.creatorId to user
                    }
                }?.toMap() ?: emptyMap()
                likesState = responses?.mapNotNull { response ->
                    responseRepository.hasLike(response.id).let { bool ->
                        response.id to bool
                    }
                }?.toMap() ?: emptyMap()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun getResponsesByCreator(id: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                responses = responseRepository.getResponsesByCreator(id).toList()
                responsesUsers = responses?.mapNotNull { response ->
                    repository.getUserById(response.creatorId).getOrNull()?.let { user ->
                        response.id to user
                    }
                }?.toMap() ?: emptyMap()
                likesState = responses?.mapNotNull { response ->
                    responseRepository.hasLike(response.id).let { bool ->
                        response.id to bool
                    }
                }?.toMap() ?: emptyMap()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }


    fun toggleLike(id: String) {
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
}