package com.gvg.mustwant.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gvg.mustwant.Enums.GoalTypeEnum
import com.gvg.mustwant.Factories.ProgressGoalsListViewModelFactory
import com.gvg.mustwant.MustWantApp
import com.gvg.mustwant.ProgressGoalsListAdapter
import com.gvg.mustwant.Repositories.ProgressGoalRepository
import com.gvg.mustwant.databinding.ActivityProgressGoalsListBinding
import com.gvg.mustwant.viewModels.ProgressGoalsListViewModel

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
        binding?.tbProgressGoalsList?.title = binding?.tbProgressGoalsList?.title.toString() + " " +
                GoalTypeEnum.values().find { it.number == goalId }?.getText(this)

        //Load obstacles from database
        progressGoalsListViewModel.setProgressesByGoalId(goalId)

    }
}