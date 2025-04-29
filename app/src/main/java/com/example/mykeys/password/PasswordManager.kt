package com.example.mykeys.password

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // шифрование данных
    private val sharedPreferences: SharedPreferences by lazy {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        EncryptedSharedPreferences.create(
            PREF_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun isPasswordSet(): Boolean {
        return sharedPreferences.contains(KEY_PASSWORD)
    }

    fun savePassword(password: String) {
        sharedPreferences.edit().putString(KEY_PASSWORD, password).apply()
    }

    fun checkPassword(password: String): Boolean {
        val savedPassword = sharedPreferences.getString(KEY_PASSWORD, "") ?: ""
        return savedPassword == password
    }

    fun saveKeyWord(keyWord: String) {
        sharedPreferences.edit().putString(KEY_Word, keyWord).apply()
    }

    fun getKeyWord(): String {
        return sharedPreferences.getString(KEY_Word, "") ?: ""
    }

    companion object {
        private const val PREF_NAME = "auth_preferences"
        private const val KEY_PASSWORD = "password"
        private const val KEY_Word = "key_word"
    }
}