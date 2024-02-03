package com.rbs.danamontest.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rbs.danamontest.data.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: User)

    @Update
    fun update(user: User)

    @Query("DELETE FROM user WHERE id = :id")
    fun delete(id: Int)

    @Query("SELECT * from user WHERE role = 'Normal User' ORDER BY id ASC")
    fun getAllData(): LiveData<List<User>>

    @Query("SELECT * from user WHERE email = :email")
    fun checkUser(email: String): LiveData<User>
}