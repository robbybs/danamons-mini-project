package com.rbs.danamontest.domain.usecase

import androidx.lifecycle.LiveData
import com.rbs.danamontest.data.local.entity.UserEntity
import com.rbs.danamontest.data.repository.UserRepository

class UserInteractor(private val repository: UserRepository) : UserUseCase {
    override fun insertUsers(user: UserEntity) {
        repository.insertUsers(user)
    }

    override fun checkDataByEmail(email: String): LiveData<UserEntity> =
        repository.checkDataByEmail(email)

    override suspend fun saveUserSession(isUserLogin: Boolean) {
        repository.saveUserSession(isUserLogin)
    }

    override suspend fun saveRole(role: String) {
        repository.saveRole(role)
    }

    override suspend fun savePassword(password: String) {
        repository.savePassword(password)
    }

    override fun getUserSession(): LiveData<Boolean> = repository.getUserSession()

    override fun getRole(): LiveData<String> = repository.getRole()
}