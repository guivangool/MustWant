package com.example.musthave.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musthave.DataEntities.GoalProgressEntity
import com.example.musthave.MustWantApp
import com.example.musthave.ProgressGoalsListAdapter
import com.example.musthave.databinding.ActivityProgressGoalsListBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProgressGoalsList : AppCompatActivity() {
    private var binding: ActivityProgressGoalsListBinding?= null
    private lateinit var progressGoalsListAdapter: ProgressGoalsListAdapter

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

        setRecyclerView()


        loadProgressGoals(getIntent().getIntExtra("selectedGoal",0))

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

    private fun loadProgressGoals(goalId:Int) {
        var progressGoalList = ArrayList<GoalProgressEntity>()
        binding?.tbProgressGoalsList?.title = binding?.tbProgressGoalsList?.title.toString()  +
                when (goalId)
                {
                    1-> "YO"
                    2-> "HOGAR"
                    3-> "TRABAJO"
                    else -> "RELACIONES"
                }

        val progressGoalsListDao = (application as MustWantApp).db.goalProgressDao()
        //Load obstacles from database
        lifecycleScope.launch {
            progressGoalsListDao.getAllByGoal(goalId).collect {
                if (it != null){
                    progressGoalList = it as ArrayList<GoalProgressEntity>
                    progressGoalsListAdapter.submitList(progressGoalList)
                }
            }



        }
    }
}