package com.example.musthave.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musthave.Repositories.ObstacleRepository
import com.example.musthave.viewModels.ObstacleListViewModel

class ObstacleListViewModelFactory (
    private val repository: ObstacleRepository
)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ObstacleListViewModel::class.java)){
            return ObstacleListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}