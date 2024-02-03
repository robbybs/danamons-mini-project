package com.rbs.danamontest

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(database: UserRoomDatabase, private val preference: UserPreference) {

    private val userDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        userDao = database.UserDao()
    }

    fun insertUsers(user: User) {
        executorService.execute { userDao.insert(user) }
    }

    fun checkDataByEmail(email: String): LiveData<User> = userDao.checkUser(email)

    suspend fun saveUserSession(isUserLogin: Boolean) {
        preference.saveSession(isUserLogin)
    }

    suspend fun saveRole(role: String) {
        preference.saveRole(role)
    }

    fun getUserSession(): LiveData<Boolean> = preference.getUserSession().asLiveData()

    fun getRole(): LiveData<String> = preference.getRoleSession().asLiveData()
}