package com.example.cryptic.data.Crypto

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

object AESHelper {
    private const val AES_MODE = "AES/GCM/NoPadding"
    private const val TAG_LENGTH_BITS = 128
    private const val IV_LENGTH_BYTES = 12

    fun encrypt(plaintext: String, hexKey: String): Triple<String, String, String> {
        val key = hexStringToByteArray(hexKey)
        val iv = ByteArray(IV_LENGTH_BYTES).apply {
            SecureRandom().nextBytes(this)
        }

        val cipher = Cipher.getInstance(AES_MODE)
        val keySpec = SecretKeySpec(key, "AES")
        val gcmSpec = GCMParameterSpec(TAG_LENGTH_BITS, iv)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec)

        val encryptedBytes = cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))

        val ciphertext = encryptedBytes.copyOfRange(0, encryptedBytes.size - TAG_LENGTH_BITS / 8)
        val tag = encryptedBytes.copyOfRange(encryptedBytes.size - TAG_LENGTH_BITS / 8, encryptedBytes.size)

        return Triple(
            Base64.encodeToString(ciphertext, Base64.NO_WRAP),
            Base64.encodeToString(iv, Base64.NO_WRAP),
            Base64.encodeToString(tag, Base64.NO_WRAP)
        )
    }

    fun decrypt(ciphertextBase64: String, ivBase64: String, tagBase64: String, hexKey: String): String {
        val key = hexStringToByteArray(hexKey)
        val iv = Base64.decode(ivBase64, Base64.NO_WRAP)
        val ciphertext = Base64.decode(ciphertextBase64, Base64.NO_WRAP)
        val tag = Base64.decode(tagBase64, Base64.NO_WRAP)

        val cipher = Cipher.getInstance(AES_MODE)
        val keySpec = SecretKeySpec(key, "AES")
        val gcmSpec = GCMParameterSpec(TAG_LENGTH_BITS, iv)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec)

        val encryptedBytes = ciphertext + tag
        return String(cipher.doFinal(encryptedBytes), Charsets.UTF_8)
    }

    private fun hexStringToByteArray(hex: String): ByteArray {
        require(hex.length % 2 == 0) { "долна быть четная длина" }
        return hex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
    }
}