package com.gvg.mustwant.Repositories

import com.gvg.mustwant.Dao.ConfigurationDao
import com.gvg.mustwant.DataEntities.GoalEntity

class SelectGoalsRepository (val configurationDao:ConfigurationDao) {


    suspend fun getGoals () : List<GoalEntity>
    {
        return configurationDao.getGoals()
    }
    suspend fun updateGoal (goal:GoalEntity){
        configurationDao.updateGoal(goal)
    }
}