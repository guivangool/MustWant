package com.gvg.mustwant.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gvg.mustwant.DataEntities.InspirationEntity
import com.gvg.mustwant.Repositories.InspirationRepository
import kotlinx.coroutines.launch

class InspirationViewModel (private val repository : InspirationRepository) : ViewModel() {
    private val _inspirations : MutableLiveData<List<InspirationEntity>> = MutableLiveData<List<InspirationEntity>>()
    val inspirations : LiveData<List<InspirationEntity>>
        get() = _inspirations

    init {
        loadInspirations()
    }
    fun loadInspirations() {
        viewModelScope.launch {
             val inspirationsList = repository.loadInspirations()
             _inspirations.postValue(inspirationsList)
        }
    }

    fun insert( goalID: Int,  phrase:String,image: String ){
        viewModelScope.launch{
            repository.insert(InspirationEntity(null,goalID,phrase,image))
        }
    }

    fun update(goalID: Int,  phrase:String,image: String){
        viewModelScope.launch{
            repository.update(goalID,phrase,image)
        }
    }
}