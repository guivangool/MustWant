package com.gvg.mustwant.viewModels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gvg.mustwant.DataEntities.User
import com.gvg.mustwant.Repositories.UserRepository
import kotlinx.coroutines.launch
class UserViewModel (private val repository : UserRepository) : ViewModel() {
    private var _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

    init {
        getUser()
    }
    fun insert( user: User){
        viewModelScope.launch{
            repository.insertUser(user)
        }
    }
    fun getUser(){
        viewModelScope.launch{
            val user = repository.getUser()
            _user.postValue(user)
        }
    }
}