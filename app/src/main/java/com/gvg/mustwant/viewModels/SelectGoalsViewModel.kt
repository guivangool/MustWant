package com.gvg.mustwant.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gvg.mustwant.DataEntities.GoalEntity
import com.gvg.mustwant.Repositories.SelectGoalsRepository
import kotlinx.coroutines.launch

class SelectGoalsViewModel( val repository: SelectGoalsRepository) : ViewModel() {
    val goals : MutableLiveData<List<GoalEntity>> = MutableLiveData<List<GoalEntity>>()

    init {
        getGoals()
    }
    fun updateGoal(goal: GoalEntity){
        viewModelScope.launch{
            repository.updateGoal(goal)
        }
    }

    fun getGoals() {
        viewModelScope.launch(){
            val goalsList = repository.getGoals()
            goals.postValue(goalsList)
        }
    }
}