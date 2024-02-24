package com.rbs.danamontest.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.rbs.danamontest.domain.usecase.HomeUseCase
import kotlinx.coroutines.launch

class HomeViewModel(private val homeUseCase: HomeUseCase) : ViewModel() {

    val getDataFromApi = homeUseCase.getData().cachedIn(viewModelScope)
    val getUserData = homeUseCase.getAllUser()
    val getUserPassword = homeUseCase.getPassword()

    fun deleteData(id: Int) {
        homeUseCase.delete(id)
    }

    fun saveSession(userLogin: Boolean) {
        viewModelScope.launch {
            homeUseCase.saveUserSession(userLogin)
        }
    }

    fun updateData(id: Int, username: String) {
        homeUseCase.updateData(id, username)
    }
}