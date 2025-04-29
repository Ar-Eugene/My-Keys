package com.example.mykeys.password

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREF_NAME, Context.MODE_PRIVATE
    )

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