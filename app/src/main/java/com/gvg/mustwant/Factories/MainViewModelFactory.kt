package com.gvg.mustwant.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gvg.mustwant.Repositories.MainRepository
import com.gvg.mustwant.viewModels.MainViewModel

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