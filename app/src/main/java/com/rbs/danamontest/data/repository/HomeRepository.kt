package com.rbs.danamontest.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.rbs.danamontest.data.network.ApiService
import com.rbs.danamontest.data.model.User
import com.rbs.danamontest.data.database.UserDao
import com.rbs.danamontest.data.database.UserRoomDatabase
import com.rbs.danamontest.data.model.PhotoItem
import com.rbs.danamontest.utils.PhotoPagingSource
import com.rbs.danamontest.utils.UserPreference
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HomeRepository(
    private val apiService: ApiService,
    private val database: UserRoomDatabase,
    private val preference: UserPreference
) {

    private val userDao: UserDao = database.UserDao()
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    fun getData(): LiveData<PagingData<PhotoItem>> {
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

    fun getAllDataFromDatabase(): LiveData<List<User>> = database.UserDao().getAllData()

    fun delete(id: Int) {
        executorService.execute { userDao.delete(id) }
    }

    suspend fun saveUserSession(isUserLogin: Boolean) {
        preference.saveSession(isUserLogin)
    }
}