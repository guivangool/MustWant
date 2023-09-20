package com.gvg.mustwant.DataEntities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "goal_progress_table")
 data class GoalProgressEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val goalID: Int,
    val progressDate: Date?,
    val goalProgress: Int,
    var  totalProgress: Int)

