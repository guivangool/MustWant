package com.example.musthave.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musthave.DataEntities.GoalEntity
import com.example.musthave.DataEntities.GoalProgressEntity
import com.example.musthave.Enums.GoalTypeEnum
import com.example.musthave.Factories.ProgressGoalViewModelFactory
import com.example.musthave.Fragments.AcceptCancel
import com.example.musthave.GoalProgressAdapter
import com.example.musthave.Interfaces.OnAcceptCancelButtonClickListener
import com.example.musthave.MustWantApp
import com.example.musthave.R
import com.example.musthave.Repositories.ProgressGoalRepository
import com.example.musthave.databinding.ActivityProgressGoalsBinding
import com.example.musthave.viewModels.ProgressGoalViewModel
import java.util.*
import kotlin.collections.ArrayList

class ProgressGoals : AppCompatActivity(), OnAcceptCancelButtonClickListener {

    private var binding: ActivityProgressGoalsBinding? = null
    private var goalList = ArrayList<String>()
    private lateinit var goalProgressAdapter: GoalProgressAdapter
    val goalProgressList = ArrayList<GoalProgressEntity>()

    private var stateGoalMe = 0
    private var stateGoalWork = 0
    private var stateGoalHome = 0
    private var stateGoalRelations = 0

    private lateinit var progressGoalViewModel: ProgressGoalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding
        binding = ActivityProgressGoalsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //Create ViewModel
        //Dao
        val progressGoalDao = (application as MustWantApp).db.goalProgressDao()
        //Repository
        val repository = ProgressGoalRepository(progressGoalDao)
        //Factory
        val factory = ProgressGoalViewModelFactory(repository)
        //ViewModel
        progressGoalViewModel =
            ViewModelProvider(this, factory).get(ProgressGoalViewModel::class.java)

        setRecyclerView()
        loadProgressGoals()

        //Add the fragment AcceptCancel programmatically
        if (savedInstanceState == null) {
            val fragment = AcceptCancel()
            val fragmentTransaction  = supportFragmentManager.beginTransaction()
            fragment.setOnAcceptCancelButtonClickListener(this)
            fragmentTransaction.add(R.id.fragment_accept_cancel,fragment)
            fragmentTransaction.commit()
        }

        goalProgressAdapter.setOnClickListener(object : GoalProgressAdapter.OnClickListener {
            override fun onCLick(goal: String, stateGoal: Int) {
                when (goal) {
                    GoalTypeEnum.ME.getText(applicationContext) -> {
                        stateGoalMe = stateGoal
                    }
                    GoalTypeEnum.WORK.getText(applicationContext) -> {
                        stateGoalWork = stateGoal
                    }
                    GoalTypeEnum.HOME.getText(applicationContext) -> {
                        stateGoalHome = stateGoal
                    }
                    GoalTypeEnum.RELATIONS.getText(applicationContext) -> {
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
        goalProgressAdapter = GoalProgressAdapter(this)
        binding?.rvMyGoalsProgress?.adapter = goalProgressAdapter
    }

    private fun loadProgressGoals() {
        var goalList = getIntent().extras?.get("goalList") as ArrayList<Int>

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
            GoalTypeEnum.ME.number -> result = stateGoalMe
            GoalTypeEnum.HOME.number -> result = stateGoalHome
            GoalTypeEnum.WORK.number -> result = stateGoalWork
            GoalTypeEnum.RELATIONS.number -> result = stateGoalRelations
        }
        if (result == 2) result = -1
        return result
    }
    override fun onAcceptButtonCLicked() {
        val nowDate = getNowDate()
        for (goal in goalProgressList) {
            progressGoalViewModel.goalId.value = goal.goalID
            progressGoalViewModel.progressDate.value = nowDate
            progressGoalViewModel.goalProgress.value = getCurrentState(goal.goalID)
            progressGoalViewModel.totalProgress.value = 0
            progressGoalViewModel.insert()
        }
        finish()
    }

    override fun onCancelButtonCLicked() {
        finish()
    }
}











