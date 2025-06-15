package com.example.gearhubmobile.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gearhubmobile.data.models.Community
import com.example.gearhubmobile.data.models.CommunityDto
import com.example.gearhubmobile.data.models.Thread
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
class HomeViewModel @Inject constructor(
    private val communityRepository: CommunityRepository,
    private val threadRepository: ThreadRepository
) : ViewModel() {


    var communities by mutableStateOf<List<CommunityDto>>(emptyList())
    var threads by mutableStateOf<List<Thread>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    suspend fun loadThreads() {
        threads = threadRepository.getAllThreads()
    }

    fun loadCommunities() {
        viewModelScope.launch {
            try {
                communities = communityRepository.getAllCommunities()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }


}