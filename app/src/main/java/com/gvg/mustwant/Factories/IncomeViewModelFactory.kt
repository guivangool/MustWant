package com.gvg.mustwant.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gvg.mustwant.Repositories.IncomeRepository
import com.gvg.mustwant.viewModels.IncomeViewModel

class IncomeViewModelFactory (
    private val repository: IncomeRepository
)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(IncomeViewModel::class.java)){
            return IncomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}