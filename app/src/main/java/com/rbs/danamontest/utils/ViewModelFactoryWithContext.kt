package com.rbs.danamontest.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rbs.danamontest.ui.home.HomeViewModel
import com.rbs.danamontest.ui.main.MainViewModel
import com.rbs.danamontest.ui.register.RegisterViewModel
import com.rbs.danamontest.di.Injection

class ViewModelFactoryWithContext(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Injection.userRepository(context)) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(Injection.userRepository(context)) as T
        } else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(Injection.homeRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}