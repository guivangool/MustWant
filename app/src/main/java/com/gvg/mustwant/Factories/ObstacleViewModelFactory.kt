package com.gvg.mustwant.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gvg.mustwant.Repositories.ObstacleRepository
import com.gvg.mustwant.viewModels.ObstacleViewModel

class ObstacleViewModelFactory (
    private val repository: ObstacleRepository)
    : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(ObstacleViewModel::class.java)){
                return ObstacleViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown View Model class")
        }
}