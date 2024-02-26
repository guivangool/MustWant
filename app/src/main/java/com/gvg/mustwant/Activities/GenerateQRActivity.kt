package com.gvg.mustwant.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gvg.mustwant.DataEntities.User
import com.gvg.mustwant.Factories.IncomeViewModelFactory
import com.gvg.mustwant.Factories.UserViewModelFactory
import com.gvg.mustwant.MustWantApp
import com.gvg.mustwant.R
import com.gvg.mustwant.Repositories.IncomeRepository
import com.gvg.mustwant.Repositories.UserRepository
import com.gvg.mustwant.databinding.ActivityFirstBinding
import com.gvg.mustwant.databinding.ActivityGenerateQrBinding
import com.gvg.mustwant.viewModels.IncomeViewModel
import com.gvg.mustwant.viewModels.UserViewModel

class GenerateQRActivity : AppCompatActivity() {
    private var binding: ActivityGenerateQrBinding? = null
    private lateinit var incomeViewModel: IncomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding
        binding = ActivityGenerateQrBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        observeQR()

        //User press "Back" button
        binding?.btnBack?.setOnClickListener {
            finish()
        }
    }
    private fun observeQR()
    {
        val incomeDao = (application as MustWantApp).db.incomeDao()
        val incomeRepository = IncomeRepository(incomeDao)
        val incomeFactory = IncomeViewModelFactory(incomeRepository)
        incomeViewModel = ViewModelProvider(this,incomeFactory).get(IncomeViewModel::class.java)

        incomeViewModel.qr.observe(this, Observer { qr ->
            binding?.ivQr?.setImageBitmap(qr)
        })

        incomeViewModel.generateQRCode(incomeRepository)
    }
}