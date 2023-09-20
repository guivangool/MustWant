package com.gvg.mustwant.DataEntities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goal_inspiration_table")
data class InspirationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val goalId: Int,
    val phrase: String = "",
    var image: String = ""
)