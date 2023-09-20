package com.gvg.mustwant.viewModels

import androidx.lifecycle.*
import com.gvg.mustwant.DataEntities.GoalEntity
import kotlinx.coroutines.launch
import com.gvg.mustwant.DomainEntities.MainMessage
import com.gvg.mustwant.Enums.GoalTypeEnum
import com.gvg.mustwant.Enums.MainMessageEnum
import com.gvg.mustwant.Repositories.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class MainViewModel ( val repository:MainRepository) : ViewModel() {
    val goalsSelection : MutableLiveData<List<GoalEntity>> = MutableLiveData<List<GoalEntity>>()

    private var _mainMessage = MutableLiveData<MainMessage>()
    private var _amountPendingTasks = MutableLiveData<Int>()

    val mainMessage:LiveData<MainMessage>
        get() = _mainMessage

    val amountPendingTasks:MutableLiveData<Int>
        get() = _amountPendingTasks

    init {
        getSelectedGoals()
    }

    fun getSelectedGoals() {
        viewModelScope.launch {

            var selectedGoals = repository.getSelectedGoals()
            if (selectedGoals.isEmpty()) {
                if (repository.getGoals().isEmpty()) {
                    //Insert all goals (selected = false)
                    repository.insertGoal(GoalEntity(GoalTypeEnum.ME.number, false,0,0,0))
                    repository.insertGoal(GoalEntity(GoalTypeEnum.HOME.number, false,0,0,0))
                    repository.insertGoal(GoalEntity(GoalTypeEnum.RELATIONS.number, false,0,0,0))
                    repository.insertGoal(GoalEntity(GoalTypeEnum.WORK.number, false,0,0,0))
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

        var lastRecordMe = 0
        var lastRecordHome = 0
        var lastRecordWork = 0
        var lastRecordRelations = 0

        viewModelScope.launch (Dispatchers.IO) {
            val percentajesData = repository.goalProgressDao.getAllTotalProgress()
            var isFirst = 1

            for (goalProgressTotal in percentajesData) {
                if (goalProgressTotal.totalProgress.compareTo(0) > 0)
                    substract = 1
                else if (goalProgressTotal.totalProgress.compareTo(0) < 0)
                    substract = -1
                else
                    substract = 0

                when (goalProgressTotal.goalID) {
                    GoalTypeEnum.ME.number -> percentajeMe = percentajeMe + substract
                    GoalTypeEnum.HOME.number -> percentajeHome = percentajeHome + substract
                    GoalTypeEnum.WORK.number -> percentajeWork = percentajeWork + substract
                    GoalTypeEnum.RELATIONS.number -> percentajeRelations = percentajeRelations + substract
                }
                if (isFirst <= 4) {
                    when (goalProgressTotal.goalID)
                    {
                        GoalTypeEnum.ME.number -> lastRecordMe = substract
                        GoalTypeEnum.HOME.number -> lastRecordHome = substract
                        GoalTypeEnum.WORK.number -> lastRecordWork = substract
                        GoalTypeEnum.RELATIONS.number -> lastRecordRelations = substract
                    }
                    isFirst += 1
                }

            }
            if (percentajeMe < 0) percentajeMe = 0
            if (percentajeHome < 0) percentajeHome = 0
            if (percentajeWork < 0) percentajeWork = 0
            if (percentajeRelations < 0) percentajeRelations = 0

            if (selectedGoals != null) {
                for (goal in selectedGoals) {
                    when (goal.goalId) {
                        GoalTypeEnum.ME.number -> {
                            goal.goalPercentaje = percentajeMe
                            goal.lastRecord = lastRecordMe
                        }
                        GoalTypeEnum.HOME.number -> {
                            goal.goalPercentaje = percentajeHome
                            goal.lastRecord = lastRecordHome
                        }
                        GoalTypeEnum.WORK.number -> {
                            goal.goalPercentaje = percentajeWork
                            goal.lastRecord = lastRecordWork
                        }
                        GoalTypeEnum.RELATIONS.number -> {
                            goal.goalPercentaje = percentajeRelations
                            goal.lastRecord = lastRecordRelations
                        }
                    }
                }
            }
            goalsSelection.postValue(selectedGoals)
        }
    }

    fun getAmountPendingTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            val amount :Int? = repository.getAmountPending()
            if (amount != null)
                _amountPendingTasks.postValue(repository.getAmountPending())
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