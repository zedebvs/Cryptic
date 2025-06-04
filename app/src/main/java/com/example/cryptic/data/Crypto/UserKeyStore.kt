package com.example.cryptic.data.Crypto


import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class UserKeyStore(context: Context) {
    private val sharedPreferences: SharedPreferences

    init {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "secure_user_key_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveAesKey(hexKey: String) {
        sharedPreferences.edit().putString("aes_key", hexKey).apply()
    }

    fun getAesKey(): String? {
        return sharedPreferences.getString("aes_key", null)
    }

    fun clearKey() {
        sharedPreferences.edit().remove("aes_key").apply()
    }
}