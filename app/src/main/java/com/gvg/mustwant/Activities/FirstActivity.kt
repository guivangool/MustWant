package com.gvg.mustwant.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gvg.mustwant.Adapters.CreditCardsAdapter
import com.gvg.mustwant.DataEntities.CreditCard
import com.gvg.mustwant.DataEntities.Income
import com.gvg.mustwant.DataEntities.User
import com.gvg.mustwant.Factories.CreditCardViewModelFactory
import com.gvg.mustwant.Factories.IncomeViewModelFactory
import com.gvg.mustwant.Factories.UserViewModelFactory
import com.gvg.mustwant.MustWantApp
import com.gvg.mustwant.Repositories.CreditCardRepository
import com.gvg.mustwant.Repositories.IncomeRepository
import com.gvg.mustwant.Repositories.UserRepository
import com.gvg.mustwant.databinding.ActivityFirstBinding
import com.gvg.mustwant.viewModels.CreditCardViewModel
import com.gvg.mustwant.viewModels.IncomeViewModel
import com.gvg.mustwant.viewModels.UserViewModel

class FirstActivity : AppCompatActivity() {

    private var binding: ActivityFirstBinding? = null
    private lateinit var incomeViewModel: IncomeViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var creditCardViewModel: CreditCardViewModel
    private var creditCardAdapter: CreditCardsAdapter? = null
    private  var idUser:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //Show initial info

        observeUser()
        observeCreditCards()
        observeBalance()



        //User press "Generate QR" button
        binding?.tvGenerateQR?.setOnClickListener {
            val intent = Intent(this@FirstActivity, GenerateQRActivity::class.java)
            startActivity(intent)
        }

        //User press "Add Credit Card" button
        binding?.tvAddCreditCards?.setOnClickListener {
            val intent = Intent(this@FirstActivity, AddCreditCardActivity::class.java)
            startActivity(intent)
        }

        //User press "Pay" button
        binding?.tvPay?.setOnClickListener {
            if (verifyIfCreditCardExist()) {
                val intent = Intent(this@FirstActivity, PayActivity::class.java)
                startActivity(intent)
            }
        }
    }
    private fun verifyIfCreditCardExist(): Boolean {
        if (creditCardViewModel.creditCards.value!!.isEmpty()) {
            Toast.makeText(this, "Debe agregar una tarjeta de crédito antes de realizar un pago.", Toast.LENGTH_LONG)
                .show()
            return false
        } else {
            return true
        }
    }
    private fun observeBalance()
    {
        val incomeDao = (application as MustWantApp).db.incomeDao()
        val incomeRepository = IncomeRepository(incomeDao)
        val incomeFactory = IncomeViewModelFactory(incomeRepository)
        incomeViewModel = ViewModelProvider(this,incomeFactory).get(IncomeViewModel::class.java)

        incomeViewModel.balance.observe(this, Observer { balance ->
            if (balance == null)
                incomeViewModel.insert(Income(null,idUser,13000.0))
            else
                binding?.tvBalance?.text = "$ " + incomeViewModel.balance.value.toString()
        })
    }

    private fun observeUser()
    {
        val userDao = (application as MustWantApp).db.userDao()
        val userRepository = UserRepository(userDao)
        val userFactory = UserViewModelFactory(userRepository)
        userViewModel = ViewModelProvider(this,userFactory).get(UserViewModel::class.java)

        userViewModel.user.observe(this, Observer { user ->
            binding?.tvWelcomeMessage?.text = "Bienvenido ${user.name} ${user.surname}"
            idUser = user.id!!
        })

    }
    private fun observeCreditCards()
    {
        val creditCardDao = (application as MustWantApp).db.creditCardDao()
        val creditCardRepository = CreditCardRepository(creditCardDao)
        val creditCardFactory = CreditCardViewModelFactory(creditCardRepository)
        creditCardViewModel = ViewModelProvider(this,creditCardFactory).get(CreditCardViewModel::class.java)

        creditCardViewModel.creditCards.observe(this, Observer { creditCards ->
            setupMyCreditCardsRecyclerView(creditCards as ArrayList<CreditCard>)
            if (creditCards.isEmpty())
            {
                //Hide Credit Cards List - Show Message
                binding?.rvMyCreditCards?.visibility = View.GONE
                binding?.tvNoCreditCards?.visibility = View.VISIBLE
            }
            else
            {
                //Show Goals List - Hide message
                binding?.rvMyCreditCards?.visibility = View.VISIBLE
                binding?.tvNoCreditCards?.visibility = View.GONE
            }
        })
    }

    private fun setupMyCreditCardsRecyclerView(myCreditCards: ArrayList<CreditCard>) {
        binding?.rvMyCreditCards?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        creditCardAdapter = CreditCardsAdapter(this,myCreditCards)
        binding?.rvMyCreditCards?.adapter = creditCardAdapter
    }
}