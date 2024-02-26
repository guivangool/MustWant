package com.gvg.mustwant.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gvg.mustwant.DataEntities.CreditCard

@Dao
interface CreditCardDao {
    @Insert
    suspend fun insert(creditCard:CreditCard)

    @Query("SELECT * FROM credit_cards")
    suspend fun getCreditCards():  List<CreditCard>
}