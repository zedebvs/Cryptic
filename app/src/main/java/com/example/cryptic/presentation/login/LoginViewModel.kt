package com.example.cryptic.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.cryptic.data.api.models.PublicProfile
import com.example.cryptic.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val profile: PublicProfile) : LoginState()
        data class Error(val message: String) : LoginState()
    }

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state

    fun login(email: String, password: String) {
        _state.value = LoginState.Loading
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            _state.value = when {
                result.isSuccess -> LoginState.Success(result.getOrThrow())
                else -> LoginState.Error(result.exceptionOrNull()?.message ?: "Неизвестная ошибка")
            }
        }
    }
}
