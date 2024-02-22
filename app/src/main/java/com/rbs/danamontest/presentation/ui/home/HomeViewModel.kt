package com.rbs.danamontest.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rbs.danamontest.data.remote.response.PhotoResponse
import com.rbs.danamontest.data.local.entity.UserEntity
import com.rbs.danamontest.data.repository.HomeRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    fun getData(): LiveData<PagingData<PhotoResponse>> =
        repository.getData().cachedIn(viewModelScope)

    fun getAllData(): LiveData<List<UserEntity>> = repository.getAllUser()

    fun deleteData(id: Int) {
        repository.delete(id)
    }

    fun saveSession(userLogin: Boolean) {
        viewModelScope.launch {
            repository.saveUserSession(userLogin)
        }
    }
}