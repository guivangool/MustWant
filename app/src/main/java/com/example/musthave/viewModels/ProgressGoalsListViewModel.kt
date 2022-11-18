package com.example.musthave.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musthave.DataEntities.GoalProgressEntity
import com.example.musthave.DataEntities.ObstacleEntity
import com.example.musthave.Repositories.ProgressGoalRepository
import kotlinx.coroutines.launch
import java.util.*

class ProgressGoalsListViewModel (private val repository : ProgressGoalRepository) : ViewModel() {
    private var _progressGoalsList = MutableLiveData<List<GoalProgressEntity>>()

    val progressGoalsList: LiveData<List<GoalProgressEntity>>
        get() = _progressGoalsList

    fun setProgressesByGoalId(goalId:Int) {
        viewModelScope.launch{
            val progressData = repository.getAllByGoal(goalId)
            if (progressData != null) {
                _progressGoalsList.postValue(progressData)
            }
        }
        }

}

