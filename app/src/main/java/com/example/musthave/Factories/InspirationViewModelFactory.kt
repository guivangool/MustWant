package com.example.musthave.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musthave.Repositories.InspirationRepository
import com.example.musthave.Repositories.ObstacleRepository
import com.example.musthave.viewModels.InspirationViewModel
import com.example.musthave.viewModels.ObstacleViewModel

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