package com.example.gearhubmobile.ui.screens.community

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gearhubmobile.data.apirest.RetrofitInstance
import com.example.gearhubmobile.data.models.Community
import kotlinx.coroutines.launch

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
class CommunityViewModel : ViewModel() {
    var communities by mutableStateOf<List<Community>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun loadCommunities() {
        viewModelScope.launch {
            isLoading = true
            try {
                val result = RetrofitInstance.communityApi.getAllCommunities()
                communities = result
            } catch (e: Exception) {
                println("Error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
}
