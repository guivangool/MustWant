package com.gvg.mustwant.viewModels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gvg.mustwant.DataEntities.Income
import com.gvg.mustwant.DataEntities.User
import com.gvg.mustwant.Repositories.IncomeRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class IncomeViewModel (private val repository : IncomeRepository) : ViewModel() {
    private var _balance = MutableLiveData<Double>()
    private var _qr = MutableLiveData<Bitmap>()
    val balance: LiveData<Double>
        get() = _balance

    val qr: LiveData<Bitmap>
        get() = _qr

    init {
        getBalance()
    }
    fun insert( income: Income){
        viewModelScope.launch{
            repository.insertIncome(income)
        }
    }
    fun getBalance() {
        viewModelScope.launch {
            _balance.postValue(repository.getBalance())
        }
    }

    fun generateQRCode(repository: IncomeRepository) {
        viewModelScope.launch {
            val mediaType = "application/x-www-form-urlencoded".toMediaTypeOrNull()
            val body = "content=http%3A%2F%2Fwww.neutrinoapi.com&width=128&height=128&fg-color=%23000000&bg-color=%23ffffff"
                .toRequestBody(mediaType)
            val url = "https://neutrinoapi-qr-code.p.rapidapi.com/qr-code"

            val response = repository.generateQR(url, body)
            if (response.isSuccessful) {
                val inputStream = response.body()?.byteStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                _qr.postValue(bitmap)
            }
        }
    }
}