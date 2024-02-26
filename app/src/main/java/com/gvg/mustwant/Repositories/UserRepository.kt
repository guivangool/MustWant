package com.gvg.mustwant.Repositories

import com.gvg.mustwant.Dao.UserDao
import com.gvg.mustwant.DataEntities.User

class UserRepository(val userDao: UserDao) {

    suspend fun insertUser (user: User)
    {
        return userDao.insert(user)
    }

    suspend fun getUser (): User
    {
        return userDao.getUSer()
    }
}