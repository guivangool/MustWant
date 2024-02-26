package com.gvg.mustwant.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gvg.mustwant.DataEntities.Payment
import com.gvg.mustwant.Repositories.PaymentRepository
import kotlinx.coroutines.launch

class PaymentViewModel (private val repository : PaymentRepository) : ViewModel() {
    fun insert( payment: Payment){
        viewModelScope.launch{
            repository.insertPayment(payment)
        }
    }
}