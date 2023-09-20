package com.gvg.mustwant.Repositories

import com.gvg.mustwant.Dao.ConfigurationDao
import com.gvg.mustwant.Dao.GoalProgressDao
import com.gvg.mustwant.Dao.InspirationDao
import com.gvg.mustwant.Dao.ObstacleDao
import com.gvg.mustwant.DataEntities.GoalEntity
import com.gvg.mustwant.DataEntities.GoalProgressEntity
import com.gvg.mustwant.DataEntities.InspirationEntity
import java.util.*

class MainRepository (val configDao:ConfigurationDao,
                      val goalProgressDao: GoalProgressDao,
                      val inspirationDao: InspirationDao,
                      val obstaclesDao: ObstacleDao) {


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

    suspend fun getAllFromYesterday(yesterday: Date): Int
    {
        return goalProgressDao.getAllFromYesterday(yesterday)
    }

      suspend fun getAllGoalInspiration(): List<InspirationEntity>
      {
          return inspirationDao.getAllGoalInspiration()
      }

    suspend fun getAmountPending(): Int  {
        return obstaclesDao.getAmountPending()
    }
}