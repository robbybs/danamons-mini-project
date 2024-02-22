package com.rbs.danamontest.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.rbs.danamontest.data.local.entity.UserEntity
import com.rbs.danamontest.data.remote.response.PhotoResponse
import com.rbs.danamontest.data.repository.HomeRepository

class HomeInteractor(private val repository: HomeRepository) : HomeUseCase {
    override fun getAllUser(): LiveData<List<UserEntity>> = repository.getAllUser()

    override fun getData(): LiveData<PagingData<PhotoResponse>> = repository.getData()

    override fun delete(id: Int) {
        repository.delete(id)
    }

    override suspend fun saveUserSession(isUserLogin: Boolean) {
        repository.saveUserSession(isUserLogin)
    }
}