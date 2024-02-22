package com.rbs.danamontest.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbs.danamontest.data.local.entity.UserEntity
import com.rbs.danamontest.data.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    fun checkData(email: String): LiveData<UserEntity> = repository.checkDataByEmail(email)

    fun saveSession(userLogin: Boolean) {
        viewModelScope.launch {
            repository.saveUserSession(userLogin)
        }
    }

    fun saveRole(role: String) {
        viewModelScope.launch {
            repository.saveRole(role)
        }
    }

    fun getUserSession(): LiveData<Boolean> = repository.getUserSession()

    fun getRole(): LiveData<String> = repository.getRole()
}