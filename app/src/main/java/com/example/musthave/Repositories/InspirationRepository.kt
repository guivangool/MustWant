package com.example.musthave.Repositories

import com.example.musthave.Dao.InspirationDao
import com.example.musthave.Dao.ObstacleDao
import com.example.musthave.DataEntities.InspirationEntity
import com.example.musthave.DataEntities.ObstacleEntity

class InspirationRepository (private val dao:InspirationDao) {
    suspend fun loadInspirations () : List<InspirationEntity>
    {
        return dao.getAllGoalInspiration()
    }

    suspend fun insert (inspirationEntity: InspirationEntity)
    {
        return dao.insert(inspirationEntity)
    }

    suspend fun update (inspirationEntity: InspirationEntity)
    {
        return dao.update(inspirationEntity)
    }

}