package com.gvg.mustwant.DataEntities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "obstacle_table")
data class ObstacleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val description: String,
    val date: String,
    var planedDate: Date?,
    var status:Int = 0
)
data class ObstacleStatus(
    val id: Int? = null,
    var status:Int = 0
)

