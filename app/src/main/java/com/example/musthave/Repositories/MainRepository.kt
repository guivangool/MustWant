package com.example.musthave.Repositories

import com.example.musthave.Dao.ConfigurationDao
import com.example.musthave.Dao.GoalProgressDao
import com.example.musthave.Dao.InspirationDao
import com.example.musthave.DataEntities.GoalEntity
import com.example.musthave.DataEntities.GoalProgressEntity
import com.example.musthave.DataEntities.InspirationEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

class MainRepository (val configDao:ConfigurationDao,
                      val goalProgressDao: GoalProgressDao,
                      val inspirationDao: InspirationDao ) {


    suspend fun getGoals() : List<GoalEntity>{
        return configDao.getGoals()
    }
    suspend fun getSelectedGoals() : List<GoalEntity>{
        return configDao.getGoalsBySelection(true)
    }
    suspend fun insertGoal (goalEntity:GoalEntity)
    {
        return configDao.insertGoal(goalEntity)
    }
    suspend fun deleteAll ()
    {
        return configDao.deleteAllDatabaseData()
    }

    suspend fun getAllTotalProgress(): List<GoalProgressEntity>
    {
       return goalProgressDao.getAllTotalProgress()
    }

    suspend fun getAllFromYesterday(yesterday: Date): Flow<Int>
    {
        return goalProgressDao.getAllFromYesterday(yesterday)
    }

      suspend fun getAllGoalInspiration(): List<InspirationEntity>
      {
          return inspirationDao.getAllGoalInspiration()
      }
}