package com.rbs.danamontest.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbs.danamontest.data.local.entity.UserEntity
import com.rbs.danamontest.domain.usecase.UserUseCase
import kotlinx.coroutines.launch

class MainViewModel(private val userUseCase: UserUseCase) : ViewModel() {

    fun checkData(email: String): LiveData<UserEntity> = userUseCase.checkDataByEmail(email)

    fun saveSession(userLogin: Boolean) {
        viewModelScope.launch {
            userUseCase.saveUserSession(userLogin)
        }
    }

    fun saveRole(role: String) {
        viewModelScope.launch {
            userUseCase.saveRole(role)
        }
    }

    fun savePassword(password: String) {
        viewModelScope.launch {
            userUseCase.savePassword(password)
        }
    }

    val getSession = userUseCase.getUserSession()
    val getUserRole = userUseCase.getRole()
}