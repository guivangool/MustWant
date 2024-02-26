package com.gvg.mustwant.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gvg.mustwant.DataEntities.Income
import com.gvg.mustwant.DataEntities.User
import com.gvg.mustwant.Factories.IncomeViewModelFactory
import com.gvg.mustwant.Factories.UserViewModelFactory
import com.gvg.mustwant.MustWantApp
import com.gvg.mustwant.R
import com.gvg.mustwant.Repositories.IncomeRepository
import com.gvg.mustwant.Repositories.UserRepository
import com.gvg.mustwant.databinding.ActivityLoginBinding
import com.gvg.mustwant.viewModels.IncomeViewModel
import com.gvg.mustwant.viewModels.UserViewModel

class LoginActivity : AppCompatActivity() {
    private var binding: ActivityLoginBinding? = null
    private lateinit var userViewModel: UserViewModel
    private lateinit var incomeViewModel: IncomeViewModel
    private  var idUser:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnlogin?.setBackgroundResource(R.drawable.shape_button_rounded)

        observeUser()
        observeBalance()

        //User press "Sign In with Google" button
        binding?.btnLoginWithGoogle?.setOnClickListener {
            val intent = Intent(this@LoginActivity, FirstActivity::class.java)
            startActivity(intent)
        }

    }
    private fun observeUser()
    {
        val userDao = (application as MustWantApp).db.userDao()
        val userRepository = UserRepository(userDao)
        val userFactory = UserViewModelFactory(userRepository)
        userViewModel = ViewModelProvider(this,userFactory).get(UserViewModel::class.java)

        userViewModel.user.observe(this, Observer { user ->
            if (user==null)
                userViewModel.insert((User(null, "837f88087bmshcb02f4f54789e45p1e79a3jsnc31926d653ef", "Guillermo", "Van Gool")))
            else
                idUser = user.id!!
        })
    }

    private fun observeBalance()
    {
        val incomeDao = (application as MustWantApp).db.incomeDao()
        val incomeRepository = IncomeRepository(incomeDao)
        val incomeFactory = IncomeViewModelFactory(incomeRepository)
        incomeViewModel = ViewModelProvider(this,incomeFactory).get(IncomeViewModel::class.java)

        incomeViewModel.balance.observe(this, Observer { balance ->
            if (balance == null)
            {
                incomeViewModel.insert(Income(null,idUser,13000.0))
            }
        })
    }
}