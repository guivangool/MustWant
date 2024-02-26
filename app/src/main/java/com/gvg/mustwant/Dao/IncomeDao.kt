package com.gvg.mustwant.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gvg.mustwant.DataEntities.Income

@Dao
interface IncomeDao {
    @Insert
    suspend fun insert(income: Income)

    @Query("SELECT sum(amount) FROM incomes")
    suspend fun getBalance(): Double

}