package com.gvg.mustwant.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.gvg.mustwant.DataEntities.CreditCard
import com.gvg.mustwant.Factories.CreditCardViewModelFactory
import com.gvg.mustwant.MustWantApp
import com.gvg.mustwant.R
import com.gvg.mustwant.Repositories.CreditCardRepository
import com.gvg.mustwant.databinding.ActivityAddCreditCardBinding
import com.gvg.mustwant.databinding.ActivityGenerateQrBinding
import com.gvg.mustwant.viewModels.CreditCardViewModel

class AddCreditCardActivity : AppCompatActivity() {
    private var binding: ActivityAddCreditCardBinding? = null
    private lateinit var creditCardViewModel: CreditCardViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding
        binding = ActivityAddCreditCardBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //User press "Cancel" button
        binding?.btnCancelFragment?.setOnClickListener {
            finish()
        }

        //User press "Accept" button
        binding?.btnAcceptFragment?.setOnClickListener {
           if (validate())
           {
               insertCreditCard(CreditCard(
                   null,
                   1,
                   binding?.etNumber?.text?.toString()!!,
                   binding?.etMonth?.text?.toString()!!.toIntOrNull()?:0,
                   binding?.etYear?.text?.toString()!!.toIntOrNull()?:0,
                   binding?.etCvv?.text?.toString()!!.toIntOrNull()?:0,
                   binding?.etFullName?.text?.toString()!!)
               )
            finish()
        }
        }
    }
    private fun validate() :Boolean
    {
        var errorMessage = ""
        if (binding?.etNumber?.text?.isEmpty()!!)
            errorMessage+="Debe ingresar el número de tarjeta.\n"
        if (binding?.etFullName?.text?.isEmpty()!!)
            errorMessage+="Debe ingresar el nombre completo que aparece en la tarjeta.\n"
        if (binding?.etMonth?.text?.isEmpty()!!)
            errorMessage+= "Debe ingresar el mes de expiración de la tarjeta.\n"
        if (binding?.etYear?.text?.isEmpty()!!)
            errorMessage+= "Debe ingresar el año de expiración de la tarjeta.\n"
        if (binding?.etCvv?.text?.isEmpty()!!)
            errorMessage+="Debe ingresar el código CVV de la tarjeta.\n"
        if (errorMessage.isNotEmpty())
        {
            showToast(errorMessage)
            return false
        }
        else
            return true
    }
    private fun showToast(message:String)
    {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
    private fun insertCreditCard(creditCard:CreditCard)
    {
        val creditCardDao = (application as MustWantApp).db.creditCardDao()
        val creditCardRepository = CreditCardRepository(creditCardDao)
        val creditCardFactory = CreditCardViewModelFactory(creditCardRepository)
        creditCardViewModel = ViewModelProvider(this,creditCardFactory).get(CreditCardViewModel::class.java)

        creditCardViewModel.insert(creditCard)
    }
}