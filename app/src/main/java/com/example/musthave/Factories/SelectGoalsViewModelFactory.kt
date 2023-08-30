package com.example.musthave.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musthave.Repositories.SelectGoalsRepository
import com.example.musthave.viewModels.SelectGoalsViewModel

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