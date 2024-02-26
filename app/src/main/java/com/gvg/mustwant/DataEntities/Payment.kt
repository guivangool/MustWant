package com.gvg.mustwant.DataEntities


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity (tableName = "payments",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = CreditCard::class,
            parentColumns = ["id"],
            childColumns = ["cardId"],
            onDelete = ForeignKey.CASCADE
        )])
data class Payment(
    @PrimaryKey (autoGenerate = true)
    val id:Int? = null,
    val userId: Int,
    val cardId: Int,
    val amount: Double,
    val description: String
)
