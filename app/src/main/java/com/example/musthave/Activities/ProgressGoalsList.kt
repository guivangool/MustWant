package com.example.musthave.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musthave.Enums.GoalTypeEnum
import com.example.musthave.Factories.ProgressGoalsListViewModelFactory
import com.example.musthave.MustWantApp
import com.example.musthave.ProgressGoalsListAdapter
import com.example.musthave.Repositories.ProgressGoalRepository
import com.example.musthave.databinding.ActivityProgressGoalsListBinding
import com.example.musthave.viewModels.ProgressGoalsListViewModel

class ProgressGoalsList : AppCompatActivity() {
    private var binding: ActivityProgressGoalsListBinding? = null
    private lateinit var progressGoalsListAdapter: ProgressGoalsListAdapter
    private lateinit var progressGoalsListViewModel: ProgressGoalsListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding
        binding = ActivityProgressGoalsListBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //Set back button to the Acion Bar
        setSupportActionBar(binding?.tbProgressGoalsList)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.tbProgressGoalsList?.setNavigationOnClickListener {
            onBackPressed()
        }

        //Create ViewModel
        //Dao
        val progressGoalDao = (application as MustWantApp).db.goalProgressDao()
        //Repository
        val repository = ProgressGoalRepository(progressGoalDao)
        //Factory
        val factory = ProgressGoalsListViewModelFactory(repository)
        //ViewModel
        progressGoalsListViewModel =
            ViewModelProvider(this, factory).get(ProgressGoalsListViewModel::class.java)

        //Observes ViewModel
        progressGoalsListViewModel.progressGoalsList.observe(this, Observer { progressGoalsList ->
            progressGoalsListAdapter.submitList(progressGoalsList)
        })

        setRecyclerView()
        loadProgressGoals(getIntent().getIntExtra("selectedGoal", 0))
    }

    private fun setRecyclerView() {
        binding?.rvProgressGoalsList?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        progressGoalsListAdapter = ProgressGoalsListAdapter()
        binding?.rvProgressGoalsList?.adapter = progressGoalsListAdapter
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun loadProgressGoals(goalId: Int) {
        binding?.tbProgressGoalsList?.title = binding?.tbProgressGoalsList?.title.toString() +
                GoalTypeEnum.values().find { it.number == goalId }?.label

        //Load obstacles from database
        progressGoalsListViewModel.setProgressesByGoalId(goalId)

    }
}