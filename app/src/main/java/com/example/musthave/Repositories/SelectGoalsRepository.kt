package com.example.musthave.Repositories

import com.example.musthave.Dao.ConfigurationDao
import com.example.musthave.DataEntities.GoalEntity

class SelectGoalsRepository (val configurationDao:ConfigurationDao) {


    suspend fun getGoals () : List<GoalEntity>
    {
        return configurationDao.getGoals()
    }
    suspend fun updateGoal (goal:GoalEntity){
        configurationDao.updateGoal(goal)
    }
}