package com.rbs.danamontest.di

import android.content.Context
import com.rbs.danamontest.data.repository.HomeRepository
import com.rbs.danamontest.utils.UserPreference
import com.rbs.danamontest.data.repository.UserRepository
import com.rbs.danamontest.data.database.UserRoomDatabase
import com.rbs.danamontest.data.network.ApiConfig
import com.rbs.danamontest.utils.dataStore

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