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

    fun checkDataByEmail(email: String): LiveData<UserEntity> = userDao.checkUser(email)
}