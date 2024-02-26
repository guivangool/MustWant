package com.gvg.mustwant.DataEntities


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "incomes",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userID"],
        onDelete = ForeignKey.CASCADE
    )])
data class Income(
    @PrimaryKey (autoGenerate = true)
    val id:Int? = null,
    val userID:Int,
    val amount:Double
)