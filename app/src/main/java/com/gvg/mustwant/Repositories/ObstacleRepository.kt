package com.gvg.mustwant.Repositories

import com.gvg.mustwant.Dao.ObstacleDao
import com.gvg.mustwant.DataEntities.ObstacleEntity
import com.gvg.mustwant.DataEntities.ObstacleStatus

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

    suspend fun getAmountPending(): Int
    {
        return obstacleDao.getAmountPending()
    }
}