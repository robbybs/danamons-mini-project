package com.rbs.danamontest.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.rbs.danamontest.data.local.LocalDataSource
import com.rbs.danamontest.data.local.database.UserRoomDatabase
import com.rbs.danamontest.data.remote.network.ApiService
import com.rbs.danamontest.data.repository.HomeRepository
import com.rbs.danamontest.data.repository.UserRepository
import com.rbs.danamontest.utils.UserPreference
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<UserRoomDatabase>().UserDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            UserRoomDatabase::class.java, "user_database"
        ).build()
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
val preferenceModule = module {
    single {
        UserPreference(androidContext().dataStore)
    }
}

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { UserPreference(get()) }
    single { LocalDataSource(get()) }
    single { UserRepository(get(), get()) }
    single { HomeRepository(get(), get(), get()) }
}