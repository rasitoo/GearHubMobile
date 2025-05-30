package com.example.gearhubmobile.ui.screens.community

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gearhubmobile.data.models.CommunityDto
import com.example.gearhubmobile.data.repositories.CommunityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val repository: CommunityRepository
) : ViewModel() {
    var communities by mutableStateOf<List<CommunityDto>>(emptyList())
    var isLoading by mutableStateOf(false)

    fun loadCommunities() {
        viewModelScope.launch {
            isLoading = true
            try {
                communities = repository.getAllCommunities()
            } catch (e: Exception) {
            } finally {
                isLoading = false
            }
        }
    }
}