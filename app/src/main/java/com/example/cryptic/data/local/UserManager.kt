//package com.example.cryptic.data.local
//
//import com.example.cryptic.data.api.models.PublicProfile
//import com.example.cryptic.data.api.models.Tokens
//import javax.inject.Inject
//import javax.inject.Singleton
//import kotlinx.coroutines.flow.Flow
//
//@Singleton
//class UserManager @Inject constructor(
//    private val securePrefs: TokenManager,
//    private val userPrefs: UserPreferences
//) {
//    val currentUser: Flow<UserData> = userPrefs.userData
//
//    fun isLoggedIn(): Boolean = securePrefs.getAccessToken() != null
//
//    suspend fun saveAuthData(tokens: Tokens, user: PublicProfile) {
//        securePrefs.saveAccessToken(tokens.accessToken)
//        securePrefs.saveRefreshToken(tokens.refreshToken)
//        userPrefs.saveUserData(UserData(user.id, user.name, user.avatar))
//    }
//
//    suspend fun logout() {
//        securePrefs.clear()
//        userPrefs.clear()
//    }
//}