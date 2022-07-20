package com.example.musthave.Dao

import androidx.room.*
import com.example.musthave.DataEntities.ConfigurationEntity

@Dao
interface ConfigurationDao {

    @Insert
    suspend fun insert(configurationEntity: ConfigurationEntity)

    @Update
    suspend fun update(configurationEntity: ConfigurationEntity)

    @Query("SELECT * FROM 'configuration_table' WHERE id = :id")
    suspend fun getConfiguration(id: Int): ConfigurationEntity

    @Query("DELETE FROM 'configuration_table'")
    suspend fun deleteAllConfiguration()

    @Query("DELETE FROM 'goal_inspiration_table'")
    suspend fun deleteAllInspirations()

    @Query("DELETE FROM 'goal_progress_table'")
    suspend fun deleteAllGoalProgresses()

    @Query("DELETE FROM 'obstacle_table'")
    suspend fun deleteAllObstacles()

    @Transaction
    suspend fun deleteAllDatabaseData()
    {
        deleteAllObstacles()
        deleteAllInspirations()
        deleteAllGoalProgresses()
        deleteAllConfiguration()
    }
}

