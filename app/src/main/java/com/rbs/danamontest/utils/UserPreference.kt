package com.rbs.danamontest.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference(private val dataStore: DataStore<Preferences>) {

    private val userKey = booleanPreferencesKey("user_login")
    private val roleKey = stringPreferencesKey("role")
    private val passwordKey = stringPreferencesKey("password")

    fun getUserSession(): Flow<Boolean> = dataStore.data.map {
        it[userKey] ?: false
    }

    fun getRoleSession(): Flow<String> = dataStore.data.map {
        it[roleKey] ?: ""
    }

    fun getPassword(): Flow<String> = dataStore.data.map {
        it[passwordKey] ?: ""
    }

    suspend fun saveSession(isUserLogin: Boolean) {
        dataStore.edit {
            it[userKey] = isUserLogin
        }
    }

    suspend fun saveRole(role: String) {
        dataStore.edit {
            it[roleKey] = role
        }
    }

    suspend fun savePassword(password: String) {
        dataStore.edit {
            it[passwordKey] = password
        }
    }
}