package com.gvg.mustwant.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gvg.mustwant.Repositories.SelectGoalsRepository
import com.gvg.mustwant.viewModels.SelectGoalsViewModel

class SelectGoalsViewModelFactory (
    private val repository: SelectGoalsRepository
)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SelectGoalsViewModel::class.java)){
            return SelectGoalsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}