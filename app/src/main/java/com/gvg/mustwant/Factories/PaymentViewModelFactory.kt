package com.gvg.mustwant.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gvg.mustwant.Repositories.PaymentRepository
import com.gvg.mustwant.viewModels.PaymentViewModel

class PaymentViewModelFactory (
    private val repository: PaymentRepository
)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PaymentViewModel::class.java)){
            return PaymentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}