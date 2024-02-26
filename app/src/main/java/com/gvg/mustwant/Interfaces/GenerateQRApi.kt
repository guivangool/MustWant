package com.gvg.mustwant.Interfaces

import com.gvg.mustwant.DataEntities.User
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

import retrofit2.http.Url

interface GenerateQRApi {
    @Headers(
        "content-type: application/x-www-form-urlencoded",
        "X-RapidAPI-Key: 837f88087bmshcb02f4f54789e45p1e79a3jsnc31926d653ef",
        "X-RapidAPI-Host: neutrinoapi-qr-code.p.rapidapi.com"
    )
    @POST
    suspend fun generateQRCode(
        @Url url: String,
        @Body body: RequestBody
    ): Response<ResponseBody>
}
