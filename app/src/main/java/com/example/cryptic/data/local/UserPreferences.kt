package com.example.cryptic.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.map

class UserPreferences(private val dataStore: DataStore<Preferences>) {
    companion object {
        val USER_ID = intPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_AVATAR = stringPreferencesKey("user_avatar")
    }

    val userData = dataStore.data.map { prefs ->
        UserData(
            id = prefs[USER_ID] ?: 0,
            name = prefs[USER_NAME] ?: "",
            avatar = prefs[USER_AVATAR] ?: ""
        )
    }

    suspend fun saveUserData(data: UserData) {
        dataStore.edit { prefs ->
            prefs[USER_ID] = data.id
            prefs[USER_NAME] = data.name
            prefs[USER_AVATAR] = data.avatar
        }
    }

    suspend fun clear() = dataStore.edit { it.clear() }
}

data class UserData(
    val id: Int,
    val name: String,
    val avatar: String
)