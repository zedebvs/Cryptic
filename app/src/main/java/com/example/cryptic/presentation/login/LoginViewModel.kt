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
            val result = authRepository.login(email, password)
            if (result.isSuccess) {
                RSAHelper.generateRSAKeyPairIfNeeded()
                val publicKey = RSAHelper.getPublicKey()

                Log.d("PUBLIC_KEY", Base64.encodeToString(publicKey.encoded, Base64.NO_WRAP))

                val publicKeyBase64 = Base64.encodeToString(publicKey.encoded, Base64.NO_WRAP)

                val encryptedAES = authRepository.fetchEncryptedAESKey(publicKeyBase64)
                if (encryptedAES != null) {
                    try {
                        Log.d("KEY_RAW_LENGTH", "Encrypted key length: ${encryptedAES.size}")
                        val decryptedAES = RSAHelper.decryptWithPrivateKey(encryptedAES)
                        val aesHex = decryptedAES.joinToString("") { "%02x".format(it) }
                        userKeyStore.saveAesKey(aesHex)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        _state.value = LoginState.Error("Ошибка расшифровки AES-ключа: ${e::class.java.simpleName} ${e.message}")
                        return@launch
                    }
                } else {
                    _state.value = LoginState.Error("Не удалось получить AES-ключ с сервера")
                    return@launch
                }

                _state.value = LoginState.Success(result.getOrThrow())
            } else {
                _state.value = LoginState.Error(result.exceptionOrNull()?.message ?: "Неизвестная ошибка")
            }
        }
    }
}
