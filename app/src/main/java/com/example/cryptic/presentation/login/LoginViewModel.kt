package com.example.cryptic.presentation.login

import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptic.data.Crypto.UserKeyStore
import com.example.cryptic.data.Crypto.RSAHelper
import kotlinx.coroutines.launch
import com.example.cryptic.data.api.models.PublicProfile
import com.example.cryptic.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userKeyStore: UserKeyStore
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
            val result = runCatching { authRepository.login(email, password).getOrThrow() }
                .fold(
                    onSuccess = { handleLoginSuccess(it) },
                    onFailure = { LoginState.Error(it.message ?: "неизвестная ошибка") }
                )
            _state.value = result
        }
    }

    private suspend fun handleLoginSuccess(profile: PublicProfile): LoginState {
        RSAHelper.generateRSAKeyPairIfNeeded()
        val publicKey = RSAHelper.getPublicKey()
        val publicKeyBase64 = Base64.encodeToString(publicKey.encoded, Base64.NO_WRAP)

        val encryptedAES = authRepository.fetchEncryptedAESKey(publicKeyBase64)
            ?: return LoginState.Error("не удалось получить AES-ключ с сервера")

        return try {
            val decryptedAES = RSAHelper.decryptWithPrivateKey(encryptedAES)
            val aesHex = decryptedAES.joinToString("") { "%02x".format(it) }
            userKeyStore.saveAesKey(aesHex)
            LoginState.Success(profile)
        } catch (e: Exception) {
            LoginState.Error("ошибка расшифровки AES-ключа: ${e.message ?: e::class.java.simpleName}")
        }
    }
}
