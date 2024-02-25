package com.rbs.danamontest.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.rbs.danamontest.domain.repository.IUserRepository
import com.rbs.danamontest.data.local.LocalDataSource
import com.rbs.danamontest.data.local.entity.UserEntity
import com.rbs.danamontest.utils.UserPreference
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(
    private val dataSource: LocalDataSource,
    private val preference: UserPreference
) : IUserRepository {

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    override suspend fun saveUserSession(isUserLogin: Boolean) {
        preference.saveSession(isUserLogin)
    }

    override suspend fun saveRole(role: String) {
        preference.saveRole(role)
    }

    override suspend fun savePassword(password: String) {
        preference.savePassword(password)
    }

    override fun getUserSession(): LiveData<Boolean> = preference.getUserSession().asLiveData()

    override fun getRole(): LiveData<String> = preference.getRoleSession().asLiveData()

    override fun insertUsers(user: UserEntity) {
        executorService.execute { dataSource.insertUsers(user) }
    }

    override fun checkUserAvailability(email: String, password: String): LiveData<UserEntity> =
        dataSource.checkUserAvailability(email, password)
}