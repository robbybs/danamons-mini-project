package com.rbs.danamontest

import androidx.lifecycle.ViewModel

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    fun insert(user: User) {
        repository.insertUsers(user)
    }
}