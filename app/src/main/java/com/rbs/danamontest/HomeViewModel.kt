package com.rbs.danamontest

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    fun getData(): LiveData<PagingData<PhotoItem>> =
        repository.getData().cachedIn(viewModelScope)

    fun getAllData(): LiveData<List<User>> = repository.getAllDataFromDatabase()

    fun deleteData(id: Int) {
        repository.delete(id)
    }

    fun saveSession(userLogin: Boolean) {
        viewModelScope.launch {
            repository.saveUserSession(userLogin)
        }
    }
}