package com.example.musthave.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.musthave.DataEntities.GoalEntity
import com.example.musthave.MustWantApp
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.example.musthave.DomainEntities.Configuration
import com.example.musthave.DomainEntities.MainMessage
import com.example.musthave.DomainEntities.MyGoal
import com.example.musthave.Enums.GoalType
import com.example.musthave.Repositories.MainRepository
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class MainViewModel ( val repository:MainRepository) : ViewModel() {
    val goalsSelection : MutableLiveData<List<GoalEntity>> = MutableLiveData<List<GoalEntity>>()

    private var _mainMessage = MutableLiveData<MainMessage>()

    val mainMessage:LiveData<MainMessage>
        get() = _mainMessage

    init {
        getSelectedGoals()
    }

    fun getSelectedGoals() {
        viewModelScope.launch {
            var selectedGoals = repository.getSelectedGoals()
            if (selectedGoals.size == 0) {
                if (repository.getGoals().size == 0) {
                    //Insert all goals (selected = false)
                    repository.insertGoal(GoalEntity(GoalType.ME.number, false,0))
                    repository.insertGoal(GoalEntity(GoalType.HOME.number, false,0))
                    repository.insertGoal(GoalEntity(GoalType.RELATIONS.number, false,0))
                    repository.insertGoal(GoalEntity(GoalType.WORK.number, false,0))
                }
            }
            getPercentajes(selectedGoals as ArrayList<GoalEntity> )
            setMainMessage(selectedGoals as ArrayList<GoalEntity>)
        }
        }

     fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
            getSelectedGoals()
        }
    }


    fun getPercentajes(selectedGoals: ArrayList<GoalEntity> ) {

        var substract = 0
        var percentajeMe = 0
        var percentajeHome = 0
        var percentajeWork = 0
        var percentajeRelations = 0

        viewModelScope.launch {
            val percentajesData = repository.goalProgressDao.getAllTotalProgress()

            for (goalProgressTotal in percentajesData) {
                if (goalProgressTotal.totalProgress.compareTo(0) > 0)
                    substract = 1
                else if (goalProgressTotal.totalProgress.compareTo(0) < 0)
                    substract = -1
                else
                    substract = 0

                when (goalProgressTotal.goalID) {
                    1 -> percentajeMe = percentajeMe + substract
                    2 -> percentajeHome = percentajeHome + substract
                    3 -> percentajeWork = percentajeWork + substract
                    4 -> percentajeRelations = percentajeRelations + substract
                }
            }
            if (percentajeMe < 0) percentajeMe = 0
            if (percentajeHome < 0) percentajeHome = 0
            if (percentajeWork < 0) percentajeWork = 0
            if (percentajeRelations < 0) percentajeRelations = 0

            if (selectedGoals != null) {
                for (goal in selectedGoals) {
                    when (goal.goalId) {
                        1 -> goal.goalPercentaje = percentajeMe
                        2 -> goal.goalPercentaje = percentajeHome
                        3 -> goal.goalPercentaje = percentajeWork
                        4 -> goal.goalPercentaje = percentajeRelations
                    }
                }
            }
            goalsSelection.postValue(selectedGoals)
        }
    }
    private fun rand(from: Int, to: Int) : Int {
        return Random.nextInt(to - from) + from
    }

    fun setMainMessage (selectedGoals: ArrayList<GoalEntity>)
    {

        val message = MainMessage(0,"","")
        viewModelScope.launch {
            if (selectedGoals.size == 0) {
                //The user did not select goals to achieve
                message.messageNumber = 1
                _mainMessage.postValue(message)
            }
            else {
                repository.getAllFromYesterday(getYesterdayDate()).collect{ progress ->
                    if (progress == 0)
                    {
                        //The user selected goals to achieve but the user did not progress them since the day before yesterday
                        message.messageNumber = 2
                        _mainMessage.postValue(message)
                    }
                    else
                    {

                        viewModelScope.launch {
                            var it = repository.getAllGoalInspiration()
                                if (it != null && it.size > 0) {
                                    var random = 0
                                    if (it.size > 1)
                                    {
                                        random = rand(0,it.size)
                                    }
                                    message.messageNumber = 4
                                    _mainMessage.postValue(message)
                                    message.image = it[random].image
                                    message.message = it[random].phrase.toString()

                                } else  {
                                    message.messageNumber = 3
                                    _mainMessage.postValue(message)
                                }
                        }
                    }
                }
            }
        }
    }

    private fun getYesterdayDate(): Date {
        var calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        return calendar.time
    }
}