package com.gvg.mustwant.viewModels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gvg.mustwant.DataEntities.CreditCard
import com.gvg.mustwant.Repositories.CreditCardRepository
import kotlinx.coroutines.launch
class CreditCardViewModel (private val repository : CreditCardRepository) : ViewModel() {
    private var _creditCards = MutableLiveData<List<CreditCard>>()
    val creditCards: LiveData<List<CreditCard>>
        get() = _creditCards

    init {
        getCreditCards()
    }
    fun insert( creditCard: CreditCard){
        viewModelScope.launch{
            repository.insertCreditCard(creditCard)
        }
    }
    fun getCreditCards(){
        viewModelScope.launch{
            _creditCards.postValue(repository.getCreditCards())
        }
    }
}