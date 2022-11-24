package com.example.musthave.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.musthave.DataEntities.ObstacleEntity
import com.example.musthave.DataEntities.ObstacleStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ObstacleDao {
    @Insert
    suspend fun insert(obstacleEntity: ObstacleEntity)

    @Update
    suspend fun update(obstacleEntity: ObstacleEntity)

    @Update (entity = ObstacleEntity::class)
    suspend fun updateStatus (obstacleStatus:ObstacleStatus)

    @Query("SELECT * FROM 'obstacle_table' order by status asc,planedDate asc ")
    suspend fun getAll(): List<ObstacleEntity>
}