package com.ismt.babybuy.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("SELECT * from user_table WHERE email = :email")
    fun checkUserExist(email: String): User?

    @Query("SELECT * FROM user_table WHERE email = :email AND password = :password")
    fun getValidUser(email: String, password: String): User
}
