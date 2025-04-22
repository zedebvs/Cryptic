package com.example.cryptic.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptic.data.api.models.PublicProfile
import com.example.cryptic.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<PublicProfile>>(emptyList())
    val searchResults: StateFlow<List<PublicProfile>> = _searchResults

    fun searchUsers(query: String) {
        viewModelScope.launch {
            val result = profileRepository.searchUsers(query)
            if (result.isSuccess) {
                _searchResults.value = result.getOrNull() ?: emptyList()
            } else {
                _searchResults.value = emptyList()
            }
        }
    }
    fun clearSearchResults() {
        _searchResults.value = emptyList()
    }
}