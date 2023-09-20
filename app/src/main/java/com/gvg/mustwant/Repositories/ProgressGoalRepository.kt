package com.gvg.mustwant.Repositories

import com.gvg.mustwant.Dao.GoalProgressDao
import com.gvg.mustwant.DataEntities.GoalProgressEntity

class ProgressGoalRepository (val progressGoalDao: GoalProgressDao) {
    suspend fun insert(goalProgressEntity: GoalProgressEntity){
        return progressGoalDao.insert(goalProgressEntity)
    }
    suspend fun getAllByGoal(goalId: Int): List<GoalProgressEntity>
    {
        return progressGoalDao.getAllByGoal(goalId)
    }
}