package com.rbs.danamontest.presentation.ui.register

import androidx.lifecycle.ViewModel
import com.rbs.danamontest.data.local.entity.UserEntity
import com.rbs.danamontest.domain.usecase.UserUseCase

class RegisterViewModel(private val userUseCase: UserUseCase) : ViewModel() {

    fun insert(user: UserEntity) {
        userUseCase.insertUsers(user)
    }
}