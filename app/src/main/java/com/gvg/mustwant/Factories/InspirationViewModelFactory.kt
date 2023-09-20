package com.gvg.mustwant.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gvg.mustwant.Repositories.InspirationRepository
import com.gvg.mustwant.viewModels.InspirationViewModel

class InspirationViewModelFactory (
    private val repository: InspirationRepository)
    : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(InspirationViewModel::class.java)){
                return InspirationViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown View Model class")
        }
}