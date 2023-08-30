package com.example.musthave.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musthave.Repositories.ObstacleRepository
import com.example.musthave.viewModels.ObstacleViewModel

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