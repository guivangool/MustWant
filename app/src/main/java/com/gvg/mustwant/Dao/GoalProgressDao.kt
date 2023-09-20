package com.gvg.mustwant.Dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gvg.mustwant.DataEntities.GoalProgressEntity
import java.util.*

@Dao
interface GoalProgressDao {

    @Insert
    suspend fun insert(goalProgressEntity: GoalProgressEntity)

    @Update
    suspend fun update(goalProgressEntity: GoalProgressEntity)

    @Query("SELECT *, 1 as totalProgress FROM 'goal_progress_table' WHERE goalID = :goalId order by progressDate asc")
    suspend fun getAllByGoal(goalId: Int): List<GoalProgressEntity>

   @Query("SELECT *,sum(goalProgress) as totalProgress FROM 'goal_progress_table' group by progressDate,goalId order by progressDate desc")
       suspend fun getAllTotalProgress(): List<GoalProgressEntity>

    @Query("SELECT count(*) FROM 'goal_progress_table' WHERE progressDate >= :yesterday ")
     fun getAllFromYesterday(yesterday: Date): Int
}