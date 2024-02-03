package com.rbs.danamontest

import android.content.Context

object Injection {
    fun homeRepository(context: Context): HomeRepository {
        val apiService = ApiConfig.getService()
        val database = UserRoomDatabase.getDatabase(context)
        val preference = UserPreference.getInstance(context.dataStore)

        return HomeRepository(apiService, database, preference)
    }

    fun userRepository(context: Context): UserRepository {
        val database = UserRoomDatabase.getDatabase(context)
        val preference = UserPreference.getInstance(context.dataStore)

        return UserRepository(database, preference)
    }
}