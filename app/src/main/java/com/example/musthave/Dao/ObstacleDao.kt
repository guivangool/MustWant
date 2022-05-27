package com.example.musthave.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.musthave.DataEntities.ObstacleEntity
import com.example.musthave.DataEntities.ObstacleStatus

@Dao
interface ObstacleDao {
    @Insert
    suspend fun insert(obstacleEntity: ObstacleEntity)

    @Update
    suspend fun update(obstacleEntity: ObstacleEntity)

    @Update (entity = ObstacleEntity::class)
    suspend fun updateStatus (obstacleStatus:ObstacleStatus)

    @Query("SELECT * FROM 'obstacle_table' order by status asc,planedDate asc ")
    fun getAll(): kotlinx.coroutines.flow.Flow<List<ObstacleEntity>>
}