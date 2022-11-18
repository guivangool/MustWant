package com.example.musthave.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musthave.DataEntities.GoalProgressEntity
import com.example.musthave.DataEntities.InspirationEntity
import com.example.musthave.Repositories.ProgressGoalRepository
import kotlinx.coroutines.launch
import java.util.*

class ProgressGoalViewModel (private val repository : ProgressGoalRepository) : ViewModel() {
    val id : MutableLiveData<Int> = MutableLiveData()
    val progressDate : MutableLiveData<Date> = MutableLiveData()
    val goalId : MutableLiveData<Int> = MutableLiveData()
    val goalProgress : MutableLiveData<Int> = MutableLiveData()
    var  totalProgress: MutableLiveData<Int> = MutableLiveData()

    fun insert(){
        viewModelScope.launch{
            repository.insert(GoalProgressEntity(null,goalId.value!!, progressDate.value!!, goalProgress.value!!, totalProgress.value!!))
        }
    }
}