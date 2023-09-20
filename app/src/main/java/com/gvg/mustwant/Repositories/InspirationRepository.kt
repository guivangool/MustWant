package com.gvg.mustwant.Repositories

import com.gvg.mustwant.Dao.InspirationDao
import com.gvg.mustwant.DataEntities.InspirationEntity

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