package com.rbs.danamontest.ui.register

import androidx.lifecycle.ViewModel
import com.rbs.danamontest.data.model.User
import com.rbs.danamontest.data.repository.UserRepository

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    fun insert(user: User) {
        repository.insertUsers(user)
    }
}