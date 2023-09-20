package com.gvg.mustwant.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gvg.mustwant.DataEntities.ObstacleEntity
import com.gvg.mustwant.Repositories.ObstacleRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ObstacleViewModel (private val repository : ObstacleRepository) : ViewModel() {
    val description = MutableLiveData<String>()
    var year : MutableLiveData<Int> = MutableLiveData()
    var month : MutableLiveData<Int> = MutableLiveData()
    var day : MutableLiveData<Int> = MutableLiveData()

    init {
        //Set today as default value
        val calendar: Calendar = Calendar.getInstance()
        year.value = calendar.get(Calendar.YEAR)
        month.value = calendar.get(Calendar.MONTH)
        day.value = calendar.get(Calendar.DAY_OF_MONTH)
    }
    //The fun Insert receives a callback function as parameter to execute it after the INSERT and
    //notifies the Activity.
    fun insert(callback : () -> Unit){

        //Convert year,month and day TO String and Date
        var calendar = Calendar.getInstance()
        calendar.set(year.value!!, month.value!!, day.value!!)

        var dateString =  SimpleDateFormat(
            "yyyyMMdd",
            Locale.getDefault()
        ).format(calendar.time)

        var dateDate = calendar.time

        viewModelScope.launch {
            repository.insert(ObstacleEntity(null, description.value!!, dateString, dateDate, 0))
            callback()
        }
    }


}
