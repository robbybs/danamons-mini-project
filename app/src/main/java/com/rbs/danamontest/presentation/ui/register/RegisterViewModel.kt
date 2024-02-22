package com.rbs.danamontest.presentation.ui.register

import androidx.lifecycle.ViewModel
import com.rbs.danamontest.data.local.entity.UserEntity
import com.rbs.danamontest.data.repository.UserRepository

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    fun insert(user: UserEntity) {
        repository.insertUsers(user)
    }
}