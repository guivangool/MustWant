package com.example.musthave.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musthave.DataEntities.GoalProgressEntity
import com.example.musthave.Enums.GoalType
import com.example.musthave.GoalProgressAdapter
import com.example.musthave.MustWantApp
import com.example.musthave.databinding.ActivityProgressGoalsBinding
import kotlinx.coroutines.launch
import java.sql.Array
import java.util.*
import kotlin.collections.ArrayList

class ProgressGoals : AppCompatActivity() {

    private var binding: ActivityProgressGoalsBinding? = null
    private var goalList = ArrayList<String>()
    private lateinit var goalProgressAdapter: GoalProgressAdapter
    val goalProgressList = ArrayList<GoalProgressEntity>()

    private var stateGoalMe = 0
    private var stateGoalWork = 0
    private var stateGoalHome = 0
    private var stateGoalRelations = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding
        binding = ActivityProgressGoalsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //Set back button to the Acion Bar
        setSupportActionBar(binding?.tbProgressGoals)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        setRecyclerView()
        loadProgressGoals()

        binding?.tbProgressGoals?.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.btnConfirm?.setOnClickListener {
            lifecycleScope.launch {

                val nowDate = getNowDate()

                val goalProgressDao = (application as MustWantApp).db.goalProgressDao()
                for (goal in goalProgressList) {
                    goalProgressDao.insert(
                        GoalProgressEntity(
                            null,
                            goal.goalID,
                            nowDate,
                            getCurrentState(goal.goalID),
                            0
                        )
                    )
                }
                finish()
            }

        }

        //Cancel button is pressed
        binding?.btnCancel?.setOnClickListener {
            finish()
        }

        goalProgressAdapter.setOnClickListener(object : GoalProgressAdapter.OnClickListener {
            override fun onCLick(goal: String, stateGoal: Int) {
                when (goal) {
                    GoalType.ME.label -> {
                        stateGoalMe = stateGoal
                    }
                    GoalType.WORK.label -> {
                        stateGoalWork = stateGoal
                    }
                    GoalType.HOME.label -> {
                        stateGoalHome = stateGoal
                    }
                    GoalType.RELATIONS.label -> {
                        stateGoalRelations = stateGoal
                    }
                }
            }
        })
    }

    private fun getNowDate(): Date {
        return java.util.Calendar.getInstance().time
    }

    private fun setRecyclerView() {
        binding?.rvMyGoalsProgress?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        goalProgressAdapter = GoalProgressAdapter()
        binding?.rvMyGoalsProgress?.adapter = goalProgressAdapter
    }

    private fun loadProgressGoals() {
        var goalList = ArrayList<Int>()

        goalList = getIntent().extras?.get("goalList") as ArrayList<Int>

        for (goal in goalList) {
            goalProgressList.add(GoalProgressEntity(null, goal,Date() ,0,0))
        }

        goalProgressAdapter.submitList(goalProgressList)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun getCurrentState(goalId: Int): Int {
        var result = 0
        when (goalId) {
            GoalType.ME.number -> result = stateGoalMe
            GoalType.HOME.number -> result = stateGoalHome
            GoalType.WORK.number -> result = stateGoalWork
            GoalType.RELATIONS.number -> result = stateGoalRelations
        }
        if (result == 2) result = -1
        return result
    }
}











