package com.gvg.mustwant.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gvg.mustwant.Repositories.CreditCardRepository
import com.gvg.mustwant.Repositories.UserRepository
import com.gvg.mustwant.viewModels.CreditCardViewModel
import com.gvg.mustwant.viewModels.UserViewModel

class CreditCardViewModelFactory (
    private val repository: CreditCardRepository
)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CreditCardViewModel::class.java)){
            return CreditCardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}