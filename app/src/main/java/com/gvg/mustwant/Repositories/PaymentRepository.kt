package com.gvg.mustwant.Repositories

import com.gvg.mustwant.Dao.PaymentDao
import com.gvg.mustwant.DataEntities.Payment
class PaymentRepository(val paymentDao: PaymentDao) {
    suspend fun insertPayment(payment: Payment)
    {
        return paymentDao.insert(payment)
    }
}