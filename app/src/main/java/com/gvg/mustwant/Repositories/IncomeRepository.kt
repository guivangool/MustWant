package com.gvg.mustwant.Repositories


import com.gvg.mustwant.Dao.IncomeDao
import com.gvg.mustwant.DataEntities.Income
import com.gvg.mustwant.DataEntities.User
import com.gvg.mustwant.Interfaces.GenerateQRApi
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class IncomeRepository(val incomeDao: IncomeDao) {
    private val qrCodeService: GenerateQRApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://neutrinoapi-qr-code.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        qrCodeService = retrofit.create(GenerateQRApi::class.java)
    }
    suspend fun insertIncome(income: Income)
    {
        return incomeDao.insert(income)
    }
    suspend fun getBalance() : Double
    {
        return incomeDao.getBalance()
    }
    suspend fun generateQR(
        url: String,
        body: RequestBody
        ): Response<ResponseBody> {
        return qrCodeService.generateQRCode(url, body)
    }

}