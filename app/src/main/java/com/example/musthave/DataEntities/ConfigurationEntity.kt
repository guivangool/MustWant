package com.example.musthave.DataEntities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName= "configuration_table")
data class ConfigurationEntity(
    @PrimaryKey
    val id: Int,
    val goalMe: Boolean,
    val goalHome: Boolean,
    val goalRelation: Boolean,
    val goalWork: Boolean
)
