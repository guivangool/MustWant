package com.gvg.mustwant.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gvg.mustwant.DataEntities.GoalProgressEntity
import com.gvg.mustwant.Enums.GoalTypeEnum
import com.gvg.mustwant.Factories.ProgressGoalViewModelFactory
import com.gvg.mustwant.Fragments.AcceptCancel
import com.gvg.mustwant.GoalProgressAdapter
import com.gvg.mustwant.Interfaces.OnAcceptCancelButtonClickListener
import com.gvg.mustwant.MustWantApp
import com.gvg.mustwant.R
import com.gvg.mustwant.Repositories.ProgressGoalRepository
import com.gvg.mustwant.databinding.ActivityProgressGoalsBinding
import com.gvg.mustwant.viewModels.ProgressGoalViewModel
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











