package com.rbs.danamontest

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    private val USER_KEY = booleanPreferencesKey("user_login")
    private val ROLE_KEY = stringPreferencesKey("role")

    fun getUserSession(): Flow<Boolean> = dataStore.data.map {
        it[USER_KEY] ?: false
    }

    fun getRoleSession(): Flow<String> = dataStore.data.map {
        it[ROLE_KEY] ?: ""
    }

    suspend fun saveSession(isUserLogin: Boolean, ) {
        dataStore.edit {
            it[USER_KEY] = isUserLogin
        }
    }

    suspend fun saveRole(role: String) {
        dataStore.edit {
            it[ROLE_KEY] = role
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}