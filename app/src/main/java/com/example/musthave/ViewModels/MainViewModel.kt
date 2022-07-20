package com.example.musthave.ViewModels

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.lifecycle.*
import com.example.musthave.DataEntities.ConfigurationEntity
import com.example.musthave.MustWantApp
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.example.musthave.*
import com.example.musthave.Dao.ConfigurationDao
import com.example.musthave.DataEntities.GoalProgressEntity
import com.example.musthave.DomainEntities.Configuration
import com.example.musthave.DomainEntities.MainMessage
import com.example.musthave.DomainEntities.MyGoal
import com.example.musthave.Enums.GoalType
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class MainViewModel (application: Application):AndroidViewModel(application) {
    private var _configuration = MutableLiveData<Configuration>()
    private var _mainMessage = MutableLiveData<MainMessage>()

    val configuration:LiveData<Configuration>
        get() = _configuration
    val mainMessage:LiveData<MainMessage>
        get() = _mainMessage

    init {
        loadConfiguration(application)
    }

    fun loadConfiguration(application: Application) {
        val configurationDao = (application as MustWantApp).db.configurationDao()
        viewModelScope.launch {
            val configurationData = configurationDao.getConfiguration(1)
            val configuration = Configuration(ArrayList(),ArrayList(),configurationData == null)
            if (configurationData != null) {
                if (configurationData.goalMe) {
                    configuration.selectedGoals.add(MyGoal(GoalType.ME.number,0))
                    configuration.selectedGoalsInt.add(GoalType.ME.number)
                }
                if (configurationData.goalHome) {
                    configuration.selectedGoals.add(MyGoal(GoalType.HOME.number,0))
                    configuration.selectedGoalsInt.add(GoalType.HOME.number)
                }
                if (configurationData.goalWork) {
                    configuration.selectedGoals.add(MyGoal(GoalType.WORK.number,0))
                    configuration.selectedGoalsInt.add(GoalType.WORK.number)
                }
                if (configurationData.goalRelation) {
                    configuration.selectedGoals.add(MyGoal(GoalType.RELATIONS.number,0))
                    configuration.selectedGoalsInt.add(GoalType.RELATIONS.number)
                }
            } else {
                configuration.isNew = true
            }
            getPercentajes(application,configuration)
            _configuration.value = configuration
            _configuration.postValue(configuration)
            setMainMessage(application)
        }
        }

    fun getPercentajes(application: Application, configuration:Configuration) {
        val goalProgressDao = (application as MustWantApp).db.goalProgressDao()
        var substract = 0
        var percentajeMe = 0
        var percentajeHome = 0
        var percentajeWork = 0
        var percentajeRelations = 0

        viewModelScope.launch {
            val percentajesData = goalProgressDao.getAllTotalProgress()

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


            for (myGoal in configuration.selectedGoals) {
                when (myGoal.goalId) {
                    1 -> myGoal.goalPercentaje = percentajeMe
                    2 -> myGoal.goalPercentaje = percentajeHome
                    3 -> myGoal.goalPercentaje = percentajeWork
                    4 -> myGoal.goalPercentaje = percentajeRelations
                }
            }
        }
    }
    private fun rand(from: Int, to: Int) : Int {
        return Random.nextInt(to - from) + from
    }

    fun setMainMessage (application: Application)
    {
        val goalProgressDao = (application as MustWantApp).db.goalProgressDao()

        val message = MainMessage(0,"","")

        viewModelScope.launch {


            if (configuration.value?.selectedGoalsInt.isNullOrEmpty()) {
                //The user did not select goals to achieve
                message.messageNumber = 1
                _mainMessage.postValue(message)
            }
            else {
                goalProgressDao.getAllFromYesterday(getYesterdayDate()).collect{ progress ->
                    if (progress == 0)
                    {
                        //The user selected goals to achieve but the user did not progress them since the day before yesterday
                        message.messageNumber = 2
                        _mainMessage.postValue(message)
                    }
                    else
                    {
                        val inspirationDao = (application as MustWantApp).db.inspitationDao()
                        viewModelScope.launch {
                            inspirationDao.getAllGoalInspiration().collect {
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
    }

    private fun getYesterdayDate(): Date {
        var calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        return calendar.time
    }
}