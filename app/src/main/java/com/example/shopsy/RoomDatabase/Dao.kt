package com.example.shopsy.RoomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun AddUser(user: User)

    @Query("select * from user where email=(:email) and password=(:password)")
    suspend fun isUserFound(email: String, password: String): User?

    @Query("select * from user")
    suspend fun getAllUser(): List<User>


    
}