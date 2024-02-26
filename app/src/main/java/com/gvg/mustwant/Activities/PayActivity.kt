package com.gvg.mustwant.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gvg.mustwant.Adapters.CreditCardsAdapter
import com.gvg.mustwant.DataEntities.CreditCard
import com.gvg.mustwant.Factories.CreditCardViewModelFactory
import com.gvg.mustwant.MustWantApp
import com.gvg.mustwant.Repositories.CreditCardRepository
import com.gvg.mustwant.databinding.ActivityPayBinding
import com.gvg.mustwant.viewModels.CreditCardViewModel

class PayActivity : AppCompatActivity() {

    private var binding: ActivityPayBinding? = null
    private var creditCardAdapter: CreditCardsAdapter? = null
    private var selectedCreditCard :Int? = null

    private lateinit var creditCardViewModel: CreditCardViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding
        binding = ActivityPayBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //User press "Cancel" button
        binding?.btnCancelFragment?.setOnClickListener {
            finish()
        }

        //User press "Pay" button
        binding?.btnAcceptFragment?.setOnClickListener {
            if (selectedCreditCard == null) {
                Toast.makeText(
                    this,
                    "Debe seleccionar una tarjeta de crédito.",
                    Toast.LENGTH_LONG
                ).show()
            }
            else
                finish()
        }
        observeCreditCards()

    }
    private fun setupMyCreditCardsRecyclerView(myCreditCards: ArrayList<CreditCard>) {
        binding?.rvMyCreditCards?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        creditCardAdapter = CreditCardsAdapter(this,myCreditCards)
        binding?.rvMyCreditCards?.adapter = creditCardAdapter
    }

    private fun observeCreditCards()
    {
        val creditCardDao = (application as MustWantApp).db.creditCardDao()
        val creditCardRepository = CreditCardRepository(creditCardDao)
        val creditCardFactory = CreditCardViewModelFactory(creditCardRepository)
        creditCardViewModel = ViewModelProvider(this,creditCardFactory).get(CreditCardViewModel::class.java)

        creditCardViewModel.creditCards.observe(this, Observer { creditCards ->
            setupMyCreditCardsRecyclerView(creditCards as ArrayList<CreditCard>)
            creditCardAdapter?.setOnClickListener(object : CreditCardsAdapter.OnClickListener {
                override fun onCLick(idCreditCard: String) {
                    selectedCreditCard = idCreditCard.toIntOrNull()

                    with (creditCardViewModel.creditCards.value?.find { it.id == selectedCreditCard })
                    {
                        binding?.tvCardSelected?.text = this?.number
                    }
                }
            })
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
}