package com.gvg.mustwant.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gvg.mustwant.DataEntities.InspirationEntity
import com.gvg.mustwant.Repositories.InspirationRepository
import kotlinx.coroutines.launch

class InspirationViewModel (private val repository : InspirationRepository) : ViewModel() {
    val inspirations : MutableLiveData<List<InspirationEntity>> = MutableLiveData<List<InspirationEntity>>()
    val id : MutableLiveData<Int> = MutableLiveData()
    val phrase : MutableLiveData<String> = MutableLiveData()
    val goalId : MutableLiveData<Int> = MutableLiveData()
    val image : MutableLiveData<String> = MutableLiveData()

    init {
        loadInspirations()
    }
    fun loadInspirations() {
        viewModelScope.launch {
             val inspirationsList = repository.loadInspirations()
             inspirations.postValue(inspirationsList)
        }
    }

    fun insert(){
        viewModelScope.launch{
            repository.insert(InspirationEntity(null,goalId.value!!,phrase.value!!,image.value!!))
        }
    }

    fun update(){
        viewModelScope.launch{
            repository.update(InspirationEntity(id.value!!,goalId.value!!,phrase.value!!,image.value!!))
        }
    }
}