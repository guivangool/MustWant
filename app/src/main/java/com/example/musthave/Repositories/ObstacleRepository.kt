package com.example.musthave.Repositories

import com.example.musthave.Dao.ObstacleDao
import com.example.musthave.DataEntities.ObstacleEntity

class ObstacleRepository (private val obstacleDao:ObstacleDao) {

    suspend fun insert (obstacle:ObstacleEntity) {
        return obstacleDao.insert(obstacle)
    }
    suspend fun getAll(): List<ObstacleEntity>
    {
        return obstacleDao.getAll()
    }
}