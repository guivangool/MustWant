package com.gvg.mustwant.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gvg.mustwant.DataEntities.ObstacleEntity
import com.gvg.mustwant.DataEntities.ObstacleStatus

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

    @Query("SELECT COUNT(id) FROM 'obstacle_table' WHERE status = 0 ")
    suspend fun getAmountPending(): Int
}