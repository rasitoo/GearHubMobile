package com.example.gearhubmobile.ui.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gearhubmobile.data.models.ResponseDTO
import com.example.gearhubmobile.data.models.Thread
import com.example.gearhubmobile.data.models.User
import com.example.gearhubmobile.data.repositories.FollowsRepository
import com.example.gearhubmobile.data.repositories.ProfileRepository
import com.example.gearhubmobile.data.repositories.ResponseRepository
import com.example.gearhubmobile.data.repositories.ThreadRepository
import com.example.gearhubmobile.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val followsRepository: FollowsRepository,
    private val sessionManager: SessionManager
) :
    ViewModel() {
    var responsesUsers by mutableStateOf<Map<String, User>>(emptyMap())
    var threads by mutableStateOf<List<Thread>?>(emptyList())
    var responses by mutableStateOf<List<ResponseDTO>?>(emptyList())
    var _users = MutableStateFlow<List<User>>(emptyList())
    var users : StateFlow<List<User>> = _users
    var user by mutableStateOf<User?>(null)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    val likesState = mutableStateMapOf<String, Boolean>()
    private val _followers = MutableStateFlow<List<User>>(emptyList())
    var followers: StateFlow<List<User>> = _followers
    private val _usersFollowing = MutableStateFlow<List<User>>(emptyList())
    var usersFollowing: StateFlow<List<User>> = _usersFollowing
    var currentUserId by mutableStateOf<String?>(null)
    var _isFollowing = MutableStateFlow(false)
    var isFollowing : StateFlow<Boolean> = _isFollowing

    init {
        viewModelScope.launch {
            currentUserId = sessionManager.getUserId()
        }
    }

    fun loadFollowers(userId: String) {
        viewModelScope.launch {
            _followers.value = followsRepository.getFollowers(userId).mapNotNull {
                repository.getUserById(it.userId.toString()).getOrNull()
            }
            _users.value = _usersFollowing.value
        }
    }

    fun loadFollowing(userId: String) {
        viewModelScope.launch {
            _usersFollowing.value = followsRepository.getFollowing(userId).mapNotNull {
                repository.getUserById(it.userId.toString()).getOrNull()
            }
            _users.value = _usersFollowing.value
        }
    }
    fun setUsers(users: List<User>) {
        viewModelScope.launch {
            _users.value = users
        }
    }

    fun checkFollowStatus(targetUserId: String) {
        viewModelScope.launch {
            currentUserId = sessionManager.getUserId()
            _isFollowing.value = followsRepository.isFollowing(currentUserId.toString(), targetUserId)
        }
    }

    fun toggleFollow(targetUserId: String) {
        viewModelScope.launch {
            currentUserId = sessionManager.getUserId()
            if (followsRepository.isFollowing(currentUserId.toString(), targetUserId)) {
                followsRepository.stopFollowing(targetUserId)
                loadFollowers(currentUserId.toString())
                _isFollowing.value = false
            } else {
                followsRepository.startFollowing(targetUserId)
                loadFollowers(currentUserId.toString())
                _isFollowing.value = true

            }

        }
    }

    fun toggleFollowing(targetUserId: String) {
        viewModelScope.launch {
            currentUserId = sessionManager.getUserId()
            if (followsRepository.isFollowing( targetUserId,currentUserId.toString())) {
                followsRepository.dropFollower(targetUserId)
                loadFollowers(currentUserId.toString())
            }

        }
    }

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
                val id = sessionManager.getUserId().toString()
                user = repository.getUserById(id).getOrNull()
                loadFollowers(id)
                loadFollowing(id)
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
                likesState.clear()
                threads?.forEach { thread ->
                    val liked = threadRepository.hasLikedThread(thread.id)
                    likesState[thread.id] = liked
                }
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
                _users.value = userList
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

                likesState.clear()
                responses?.forEach { response ->
                    val liked = responseRepository.hasLike(response.id)
                    likesState[response.id] = liked
                }
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
                        response.creatorId to user
                    }
                }?.toMap() ?: emptyMap()

                likesState.clear()
                responses?.forEach { response ->
                    val liked = responseRepository.hasLike(response.id)
                    likesState[response.id] = liked
                }
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
                val success = if (current)
                    responseRepository.unlikeResponse(id).isSuccessful
                else
                    responseRepository.likeResponse(id).isSuccessful

                if (success) {
                    likesState[id] = !current
                }
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }
}