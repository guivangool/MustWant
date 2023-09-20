package com.gvg.mustwant.viewModels

import androidx.lifecycle.*
import com.gvg.mustwant.DataEntities.ObstacleEntity
import com.gvg.mustwant.DataEntities.ObstacleStatus
import com.gvg.mustwant.Repositories.ObstacleRepository
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
    fun updateStatus(obstacleStatus: ObstacleStatus)
    {
        viewModelScope.launch{
            repository.updateStatus(obstacleStatus)
        }
    }
}