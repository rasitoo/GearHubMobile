package com.example.gearhubmobile.ui.screens.community

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gearhubmobile.data.apirest.RetrofitInstance.communityApi
import com.example.gearhubmobile.data.models.CommunityDto
import kotlinx.coroutines.launch

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
class CommunityViewModel : ViewModel() {
    var communities by mutableStateOf<List<CommunityDto>>(emptyList())
    var isLoading by mutableStateOf(false)

    fun loadCommunities() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = communityApi.getAllCommunities()
                communities = response.data
            } catch (e: Exception) {
                // Manejo de error
            } finally {
                isLoading = false
            }
        }
    }
}