package com.gvg.mustwant.DataEntities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goal_table")
data class GoalEntity(
    @PrimaryKey
    val goalId : Int,
    val selected : Boolean,
    var goalPercentaje: Int,
    var goalDays: Int,
    var lastRecord:Int
)
