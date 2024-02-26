package com.gvg.mustwant.DataEntities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity (
    tableName = "credit_cards",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )])

data class CreditCard(
    @PrimaryKey (autoGenerate = true)
    val id:Int? = null,
    val userId: Int,
    val number:String,
    val month: Int,
    val year: Int,
    val cod: Int,
    val fullName: String
)
