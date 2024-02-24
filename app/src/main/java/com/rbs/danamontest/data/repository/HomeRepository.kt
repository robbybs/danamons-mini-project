package com.rbs.danamontest.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.rbs.danamontest.domain.repository.IHomeRepository
import com.rbs.danamontest.data.local.LocalDataSource
import com.rbs.danamontest.data.remote.network.ApiService
import com.rbs.danamontest.data.local.entity.UserEntity
import com.rbs.danamontest.data.remote.response.PhotoResponse
import com.rbs.danamontest.data.remote.network.PhotoPagingSource
import com.rbs.danamontest.utils.UserPreference
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HomeRepository(
    private val apiService: ApiService,
    private val dataSource: LocalDataSource,
    private val preference: UserPreference
) : IHomeRepository {

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    override fun getData(): LiveData<PagingData<PhotoResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                PhotoPagingSource(apiService)
            }
        ).liveData
    }

    override fun getAllUser(): LiveData<List<UserEntity>> = dataSource.getAllData()

    override fun delete(id: Int) {
        executorService.execute { dataSource.delete(id) }
    }

    override suspend fun saveUserSession(isUserLogin: Boolean) {
        preference.saveSession(isUserLogin)
    }

    override fun getPassword(): LiveData<String> = preference.getPassword().asLiveData()
    override fun updateData(id: Int, username: String) {
        executorService.execute { dataSource.update(id, username) }
    }

}