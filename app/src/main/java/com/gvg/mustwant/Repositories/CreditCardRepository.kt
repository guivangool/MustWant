package com.gvg.mustwant.Repositories

import com.gvg.mustwant.Dao.CreditCardDao
import com.gvg.mustwant.Dao.UserDao
import com.gvg.mustwant.DataEntities.CreditCard
import com.gvg.mustwant.DataEntities.User

class CreditCardRepository(val creditCardDao: CreditCardDao) {
    suspend fun insertCreditCard (creditCard: CreditCard)
    {
        return creditCardDao.insert(creditCard)
    }

    suspend fun getCreditCards(): List<CreditCard>{
        return creditCardDao.getCreditCards()
    }
}