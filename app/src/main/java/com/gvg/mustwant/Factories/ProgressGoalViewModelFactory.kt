package com.gvg.mustwant.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gvg.mustwant.Repositories.ProgressGoalRepository
import com.gvg.mustwant.viewModels.ProgressGoalViewModel

class ProgressGoalViewModelFactory (
    private val repository: ProgressGoalRepository
)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ProgressGoalViewModel::class.java)){
            return ProgressGoalViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}