package com.example.musthave.Activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.musthave.MustWantApp
import com.example.musthave.Repositories.ObstacleRepository
import com.example.musthave.databinding.ActivityRemoveObstacleBinding
import com.example.musthave.viewModels.ObstacleViewModel
import com.example.musthave.Factories.ObstacleViewModelFactory
import com.example.musthave.R

class RemoveObstacle : AppCompatActivity() {
    var binding: ActivityRemoveObstacleBinding? = null
    private lateinit var obstacleViewModel: ObstacleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding
        binding = ActivityRemoveObstacleBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //DataBinding - ViewModel
        val obstacleDao = (application as MustWantApp).db.obstacleDao()
        val repository = ObstacleRepository(obstacleDao)
        val factory = ObstacleViewModelFactory(repository)
        obstacleViewModel = ViewModelProvider(this, factory).get(ObstacleViewModel::class.java)
        binding?.obstacleViewModel = obstacleViewModel
        binding?.lifecycleOwner = this

        //Set back button in the Action Bar
        setSupportActionBar(binding?.tbRemoveObstacle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding?.tbRemoveObstacle?.setNavigationOnClickListener {
            onBackPressed()
        }

        //Set minium to the DatePicker
        binding?.dpObstacle?.setMinDate(System.currentTimeMillis())

        binding?.btnCancel?.setOnClickListener {
            finish()
        }
        binding?.btnAccept?.setOnClickListener {
            if (binding?.etSetObstacleName?.text?.toString() != "") {
                //Send a callback function by parameter to be executed after the INSERT
                obstacleViewModel.insert({ finish() })
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.CREATE_OBSTACLE_SET_FIELDS_VALIDATION),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}