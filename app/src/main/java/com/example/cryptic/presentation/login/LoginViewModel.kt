package com.example.cryptic.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class User {
    var login = "admin"
    var password = "1234"
    var email = "admin@yandex.com"
}

class AuthViewModel : ViewModel() {

    private val dummyUser = User()
    private val _authResult = mutableStateOf<Boolean?>(null)

    val authResult: State<Boolean?> = _authResult
    fun checkCredentials(login: String, password: String, email: String) {
        viewModelScope.launch {
            delay(500)
            _authResult.value = (login == dummyUser.login || email == dummyUser.email) && password == dummyUser.password
        }
    }
    fun resetAuthResult() {
        _authResult.value = null
    }
}