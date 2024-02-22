package com.rbs.danamontest

import android.app.Application
import com.rbs.danamontest.di.databaseModule
import com.rbs.danamontest.di.networkModule
import com.rbs.danamontest.di.preferenceModule
import com.rbs.danamontest.di.repositoryModule
import com.rbs.danamontest.di.useCaseModule
import com.rbs.danamontest.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    preferenceModule,
                    viewModelModule,
                    useCaseModule
                )
            )
        }
    }
}