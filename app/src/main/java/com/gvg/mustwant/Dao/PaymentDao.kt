package com.gvg.mustwant.Dao

import androidx.room.Dao
import androidx.room.Insert
import com.gvg.mustwant.DataEntities.Payment

@Dao
interface PaymentDao {
    @Insert
    suspend fun insert(payment: Payment)
}