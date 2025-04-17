package com.example.cryptic.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.cryptic.data.repository.AuthRepository


class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()
    private val _authResult = mutableStateOf<Boolean?>(null)
    val authResult: State<Boolean?> = _authResult

    fun login(login: String, password: String) {
        viewModelScope.launch {
            _authResult.value = null
            try {
                val result = repository.login(login, password)
                _authResult.value = result.isSuccess
            } catch (e: Exception) {
                _authResult.value = false
            }
        }
    }

    fun resetAuthResult() {
        _authResult.value = null
    }
}