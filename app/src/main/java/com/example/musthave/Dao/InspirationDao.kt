package com.example.musthave.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.musthave.DataEntities.InspirationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InspirationDao {
    @Insert
    suspend fun insert(inspirationEntity: InspirationEntity)

    @Update
    suspend fun update(inspirationEntity: InspirationEntity)

    @Query("SELECT * FROM 'goal_inspiration_table' WHERE goalId = :goalId")
    fun getGoalInspiration(goalId: Int?): kotlinx.coroutines.flow.Flow<InspirationEntity>

    @Query("SELECT * FROM 'goal_inspiration_table'")
    fun getAllGoalInspiration(): Flow<List<InspirationEntity>>
}