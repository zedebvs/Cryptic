package com.example.cryptic.ui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptic.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    sealed class RegistrationState {
        object Idle : RegistrationState()
        object Loading : RegistrationState()
        object Success : RegistrationState()
        data class Error(val message: String) : RegistrationState()
    }

    private val _state = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val state: StateFlow<RegistrationState> = _state


    fun register(
        email: String,
        name: String,
        password: String,
        repeatPassword: String
    ) {
        when {
            email.isBlank() -> {
                _state.value = RegistrationState.Error("email не может быть пустым")
                return
            }
            name.isBlank() -> {
                _state.value = RegistrationState.Error("имя не может быть пустым")
                return
            }
            password.length < 8 -> {
                _state.value = RegistrationState.Error("Пароль должен быть не менее 8 символов")
                return
            }
            password != repeatPassword -> {
                _state.value = RegistrationState.Error("Пароли не совпадают")
                return
            }
        }

        _state.value = RegistrationState.Loading

        viewModelScope.launch {
            val result = authRepository.register(email, name, password)

            _state.value = when {
                result.isSuccess -> RegistrationState.Success
                else -> RegistrationState.Error(
                    result.exceptionOrNull()?.message ?: "Неизвестная ошибка"
                )
            }
        }
    }

    fun resetState() {
        _state.value = RegistrationState.Idle
    }
}