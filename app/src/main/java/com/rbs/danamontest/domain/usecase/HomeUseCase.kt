package com.rbs.danamontest.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.rbs.danamontest.data.local.entity.UserEntity
import com.rbs.danamontest.data.remote.response.PhotoResponse

interface HomeUseCase {
    fun getAllUser(): LiveData<List<UserEntity>>

    fun getData(): LiveData<PagingData<PhotoResponse>>

    fun delete(id: Int)

    suspend fun saveUserSession(isUserLogin: Boolean)

    fun getPassword(): LiveData<String>

    fun updateData(id: Int, username: String)
}