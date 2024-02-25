package com.rbs.danamontest.data.local

import androidx.lifecycle.LiveData
import com.rbs.danamontest.data.local.database.UserDao
import com.rbs.danamontest.data.local.entity.UserEntity

class LocalDataSource(private val userDao: UserDao) {
    fun getAllData(): LiveData<List<UserEntity>> = userDao.getAllData()

    fun insertUsers(user: UserEntity) {
        userDao.insert(user)
    }

    fun delete(id: Int) {
        userDao.delete(id)
    }

    fun checkUserAvailability(email: String, password: String): LiveData<UserEntity> = userDao.checkUserAvailability(email, password)

    fun update(id: Int, username: String) {
        userDao.update(id, username)
    }
}