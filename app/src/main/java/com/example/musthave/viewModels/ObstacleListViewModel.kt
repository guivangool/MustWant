package com.example.musthave.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.musthave.DataEntities.ObstacleEntity
import com.example.musthave.MustWantApp
import com.example.musthave.Repositories.ObstacleRepository
import kotlinx.coroutines.launch


class ObstacleListViewModel (val repository:ObstacleRepository): ViewModel() {
    private var _obstacleList = MutableLiveData<List<ObstacleEntity>>()

    val obstacleList: LiveData<List<ObstacleEntity>>
        get() = _obstacleList

    init {
        loadObstacles()
    }

 fun loadObstacles() {

    //Load obstacles from database
     viewModelScope.launch {
        val obstacleData = repository.getAll()
            if (obstacleData != null) {
                _obstacleList.postValue(obstacleData)
            }
        }
    }
}