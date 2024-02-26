package com.gvg.mustwant.DataEntities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val googleId: String,
    val name :String,
    val surname:String
)
