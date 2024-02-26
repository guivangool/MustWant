package com.gvg.mustwant.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gvg.mustwant.DataEntities.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query ("SELECT * FROM users LIMIT 1")
    suspend fun getUSer() : User
}