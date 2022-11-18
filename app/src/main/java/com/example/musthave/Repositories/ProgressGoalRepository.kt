package com.example.musthave.Repositories

import com.example.musthave.Dao.GoalProgressDao
import com.example.musthave.DataEntities.GoalProgressEntity
import kotlinx.coroutines.flow.Flow

class ProgressGoalRepository (val progressGoalDao: GoalProgressDao) {
    suspend fun insert(goalProgressEntity: GoalProgressEntity){
        return progressGoalDao.insert(goalProgressEntity)
    }
    suspend fun getAllByGoal(goalId: Int): List<GoalProgressEntity>
    {
        return progressGoalDao.getAllByGoal(goalId)
    }
}