package com.example.musthave.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musthave.Repositories.MainRepository
import com.example.musthave.viewModels.MainViewModel

class MainViewModelFactory (
    private val repository: MainRepository
)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}