package com.rbs.danamontest

import com.rbs.danamontest.data.local.entity.UserEntity

object DataDummy {
    fun getData(): List<UserEntity> {
        val userList = ArrayList<UserEntity>()
        for (i in 0..10) {
            val user = UserEntity(
                1,
                "robbybs",
                "robbybs@rbs.com",
                "123456",
                "Normal User"
            )
            userList.add(user)
        }

        return userList
    }
}