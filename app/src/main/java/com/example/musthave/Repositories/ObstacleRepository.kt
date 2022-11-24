package com.example.musthave.Repositories

import com.example.musthave.Dao.ObstacleDao
import com.example.musthave.DataEntities.ObstacleEntity
import com.example.musthave.DataEntities.ObstacleStatus
import kotlinx.coroutines.flow.Flow

class ObstacleRepository (private val obstacleDao:ObstacleDao) {

    suspend fun insert (obstacle:ObstacleEntity) {
        return obstacleDao.insert(obstacle)
    }
    suspend fun getAll(): List<ObstacleEntity>
    {
        return obstacleDao.getAll()
    }
    suspend  fun updateStatus (obstacleStatus: ObstacleStatus){
        return obstacleDao.updateStatus(obstacleStatus)
    }
}