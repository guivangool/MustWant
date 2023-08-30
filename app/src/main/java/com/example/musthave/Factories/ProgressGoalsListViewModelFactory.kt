package com.example.musthave.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musthave.Repositories.ProgressGoalRepository
import com.example.musthave.viewModels.ProgressGoalsListViewModel

class ProgressGoalsListViewModelFactory (
    private val repository: ProgressGoalRepository
)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ProgressGoalsListViewModel::class.java)){
            return ProgressGoalsListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}