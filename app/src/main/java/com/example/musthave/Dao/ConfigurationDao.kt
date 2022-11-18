package com.example.musthave.Dao

import androidx.room.*
import com.example.musthave.DataEntities.GoalEntity

@Dao
interface ConfigurationDao {


    @Query("SELECT * FROM 'goal_table'")
    suspend fun getGoals(): List<GoalEntity>

    @Query("SELECT * FROM 'goal_table' WHERE selected = :selected")
    suspend fun getGoalsBySelection(selected:Boolean): List<GoalEntity>

    @Update
    suspend fun updateGoal (goal: GoalEntity)

    @Insert
    suspend fun insertGoal(goalEntity: GoalEntity)



    @Query("DELETE FROM 'goal_inspiration_table'")
    suspend fun deleteAllInspirations()

    @Query("DELETE FROM 'goal_progress_table'")
    suspend fun deleteAllGoalProgresses()

    @Query("DELETE FROM 'obstacle_table'")
    suspend fun deleteAllObstacles()

    @Query("DELETE FROM 'goal_table'")
    suspend fun deleteAllGoals()

    @Transaction
    suspend fun deleteAllDatabaseData()
    {
        deleteAllObstacles()
        deleteAllInspirations()
        deleteAllGoalProgresses()
        deleteAllGoals()
    }
}

