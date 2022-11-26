package com.example.musthave.viewModels

import androidx.lifecycle.*
import com.example.musthave.DataEntities.GoalEntity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.example.musthave.DomainEntities.MainMessage
import com.example.musthave.Enums.GoalTypeEnum
import com.example.musthave.Enums.MainMessageEnum
import com.example.musthave.Repositories.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
                    repository.insertGoal(GoalEntity(GoalTypeEnum.ME.number, false,0))
                    repository.insertGoal(GoalEntity(GoalTypeEnum.HOME.number, false,0))
                    repository.insertGoal(GoalEntity(GoalTypeEnum.RELATIONS.number, false,0))
                    repository.insertGoal(GoalEntity(GoalTypeEnum.WORK.number, false,0))
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

        viewModelScope.launch (Dispatchers.IO) {
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
        viewModelScope.launch(Dispatchers.IO) {
            val message = MainMessage(0,"","")
            if (selectedGoals.size == 0) {
                //The user did not select goals to achieve
                message.messageNumber = MainMessageEnum.NO_SELECTED_GOALS.number
                message.message = MainMessageEnum.NO_SELECTED_GOALS.message
                _mainMessage.postValue(message)
            }
            else {
                val progress = withContext(Dispatchers.IO){ repository.getAllFromYesterday(getYesterdayDate())}
                    if (progress == 0)
                    {
                        //The user selected goals to achieve but the user did not progress them since the day before yesterday
                        message.messageNumber = MainMessageEnum.NO_GOAL_PROGRESS.number
                        message.message = MainMessageEnum.NO_GOAL_PROGRESS.message
                        _mainMessage.postValue(message)
                    }
                    else
                    {
                        var it = withContext(Dispatchers.IO){repository.getAllGoalInspiration()}
                        if (it != null && it.size > 0) {
                            var random = 0
                            if (it.size > 1)
                            {
                                random = rand(0,it.size)
                            }
                            message.messageNumber = MainMessageEnum.ALL_COMPLETED.number
                            message.image = it[random].image
                            message.message = it[random].phrase.toString()
                            _mainMessage.postValue(message)
                        }
                        else
                        {
                            message.messageNumber = MainMessageEnum.NO_INSPIRATIONS.number
                            message.message = MainMessageEnum.NO_INSPIRATIONS.message
                            _mainMessage.postValue(message)
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