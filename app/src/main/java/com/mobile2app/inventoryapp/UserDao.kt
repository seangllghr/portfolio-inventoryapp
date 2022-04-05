package com.mobile2app.inventoryapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    suspend fun addUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?

    @Delete
    fun deleteUser(user: User)

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

}