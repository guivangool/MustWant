package com.example.musthave.Repositories

import com.example.musthave.Dao.ObstacleDao
import com.example.musthave.DataEntities.ObstacleEntity

class ObstacleRepository (private val dao:ObstacleDao) {

    suspend fun insert (obstacle:ObstacleEntity) {
        return dao.insert(obstacle)
    }

}