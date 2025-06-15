package com.example.gearhubmobile.ui.screens.community

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gearhubmobile.data.models.Community
import com.example.gearhubmobile.data.models.Thread
import com.example.gearhubmobile.data.models.CommunityDto
import com.example.gearhubmobile.data.repositories.CommunityRepository
import com.example.gearhubmobile.data.repositories.ThreadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val repository: CommunityRepository,
    private val threadRepository: ThreadRepository
) : ViewModel() {
    var communities by mutableStateOf<List<CommunityDto>>(emptyList())
    var community = mutableStateOf<Community?>(null)
    var communityPosts = mutableStateOf<List<Thread>>(emptyList())
    var likesState by mutableStateOf<Map<String, Boolean>>(emptyMap())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadCommunities() {
        viewModelScope.launch {
            isLoading = true
            try {
                communities = repository.getAllCommunities()
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
            } catch (_: Exception) { }
            isLoading = false
        }
    }

    fun loadPosts(communityId: String) {
        viewModelScope.launch {
            try {
                communityPosts.value = threadRepository.getThreadsByCommunity(communityId).data
            } catch (_: Exception) { }
        }
    }

    fun toggleLike(string: String)  {}
    fun postHasLike(string: String?): Boolean {return false}
}