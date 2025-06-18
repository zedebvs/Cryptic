package com.example.cryptic.data.Crypto


import android.content.Context
import android.content.SharedPreferences
import android.util.Log
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
        Log.d("KEY_DEBUG", "Ключ который сохранился: $hexKey")
        sharedPreferences.edit().putString("aes_key", hexKey).apply()
    }

    fun getAesKey(): String? {
        //return sharedPreferences.getString("aes_key", null)
        val key = sharedPreferences.getString("aes_key", null)
        Log.d("KEY_DEBUG", "Ключ для шифрования: $key")
        return key
    }

    fun clearKey() {
        sharedPreferences.edit().remove("aes_key").apply()
    }
}