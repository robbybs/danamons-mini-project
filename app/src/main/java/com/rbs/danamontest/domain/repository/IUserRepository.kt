package com.rbs.danamontest.domain.repository

import androidx.lifecycle.LiveData
import com.rbs.danamontest.data.local.entity.UserEntity

interface IUserRepository {
    fun insertUsers(user: UserEntity)

    fun checkUserAvailability(email: String, password: String): LiveData<UserEntity>

    suspend fun saveUserSession(isUserLogin: Boolean)

    suspend fun saveRole(role: String)

    suspend fun savePassword(password: String)

    fun getUserSession(): LiveData<Boolean>

    fun getRole(): LiveData<String>
}