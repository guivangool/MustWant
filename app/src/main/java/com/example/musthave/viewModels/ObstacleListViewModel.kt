package com.example.musthave.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.musthave.DataEntities.ObstacleEntity
import com.example.musthave.MustWantApp
import kotlinx.coroutines.launch


class ObstacleListViewModel ( application: Application): AndroidViewModel(application) {
    private var _obstacleList = MutableLiveData<List<ObstacleEntity>>()

    val obstacleList: LiveData<List<ObstacleEntity>>
        get() = _obstacleList

    init {
        loadObstacles(application)
    }

 fun loadObstacles(application: Application) {
    val obstacleDao = (application as MustWantApp).db.obstacleDao()
    //Load obstacles from database
     viewModelScope.launch {
        val obstacleData = obstacleDao.getAll()
            if (obstacleData != null) {
                _obstacleList.postValue(obstacleData)
            }

        }
    }
}