package com.example.musthave.Activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.musthave.DataEntities.GoalEntity
import com.example.musthave.Enums.GoalTypeEnum
import com.example.musthave.Factories.SelectGoalsViewModelFactory
import com.example.musthave.Fragments.AcceptCancel
import com.example.musthave.Interfaces.OnAcceptCancelButtonClickListener
import com.example.musthave.MustWantApp
import com.example.musthave.R
import com.example.musthave.Repositories.SelectGoalsRepository
import com.example.musthave.databinding.ActivitySelectGoalsBinding
import com.example.musthave.viewModels.SelectGoalsViewModel


class SelectGoals : AppCompatActivity(), OnAcceptCancelButtonClickListener {

    private var binding: ActivitySelectGoalsBinding? = null
    private var GoalMeisSelected = false
    private var GoalHomeisSelected = false
    private var GoalRelationisSelected = false
    private var GoalWorkisSelected = false
    private lateinit var selectGoalsViewModel: SelectGoalsViewModel
    private var goals = ArrayList<GoalEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding
        binding = ActivitySelectGoalsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //Action Bar
        setSupportActionBar(binding?.tbSelectGoals)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.tbSelectGoals?.setNavigationOnClickListener {
            onBackPressed()
        }

        //Add the fragment AcceptCancel programmatically
        if (savedInstanceState == null) {
            val fragment = AcceptCancel()
            val fragmentTransaction  = supportFragmentManager.beginTransaction()
            fragment.setOnAcceptCancelButtonClickListener(this)
            fragmentTransaction.add(R.id.fragment_accept_cancel,fragment)
            fragmentTransaction.commit()
        }

        //Create ViewModel
        //Dao
        val configurationDao = (application as MustWantApp).db.configurationDao()

        //Repository
        val repository = SelectGoalsRepository(configurationDao)
        //Factory
        val factory = SelectGoalsViewModelFactory(repository)
        //ViewModel
        selectGoalsViewModel =
            ViewModelProvider(this, factory).get(SelectGoalsViewModel::class.java)

        //Observe ViewModel
        selectGoalsViewModel.goals.observe(this, androidx.lifecycle.Observer { goals ->
                this.goals = goals as ArrayList<GoalEntity>
                for (goal in goals) {
                    when (goal.goalId) {
                        GoalTypeEnum.ME.number -> {
                            selectedGoal(binding?.GoalMe as TextView, goal.selected)
                            GoalMeisSelected = goal.selected
                        }
                        GoalTypeEnum.HOME.number -> {
                            selectedGoal(binding?.GoalHome as TextView, goal.selected)
                            GoalHomeisSelected = goal.selected
                        }
                        GoalTypeEnum.WORK.number -> {
                            selectedGoal(binding?.GoalWork as TextView, goal.selected)
                            GoalWorkisSelected = goal.selected
                        }
                        GoalTypeEnum.RELATIONS.number -> {
                            selectedGoal(binding?.GoalRelations as TextView, goal.selected)
                            GoalRelationisSelected = goal.selected
                        }
                    }
                }

            })

        binding?.GoalMe?.setOnClickListener {
            GoalMeisSelected = !GoalMeisSelected
            selectedGoal(binding?.GoalMe as TextView, GoalMeisSelected)
        }
        binding?.GoalHome?.setOnClickListener {
            GoalHomeisSelected = !GoalHomeisSelected
            selectedGoal(binding?.GoalHome as TextView, GoalHomeisSelected)
        }
        binding?.GoalRelations?.setOnClickListener {
            GoalRelationisSelected = !GoalRelationisSelected
            selectedGoal(binding?.GoalRelations as TextView, GoalRelationisSelected)
        }
        binding?.GoalWork?.setOnClickListener {
            GoalWorkisSelected = !GoalWorkisSelected
            selectedGoal(binding?.GoalWork as TextView, GoalWorkisSelected)
        }

    }

    private fun selectedGoal(tv: TextView, selected: Boolean) {
        if (selected) {
            tv.background = ContextCompat.getDrawable(this, R.drawable.bg_selected_goals)
            tv.setTextColor(getResources().getColor(R.color.white))
        } else {
            tv.background = ContextCompat.getDrawable(this, R.drawable.bg_goals)
            tv.setTextColor(getResources().getColor(R.color.black))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onAcceptButtonCLicked() {
        selectGoalsViewModel.updateGoal(GoalEntity(GoalTypeEnum.ME.number,GoalMeisSelected,0))
        selectGoalsViewModel.updateGoal(GoalEntity(GoalTypeEnum.HOME.number,GoalHomeisSelected,0))
        selectGoalsViewModel.updateGoal(GoalEntity(GoalTypeEnum.RELATIONS.number,GoalRelationisSelected,0))
        selectGoalsViewModel.updateGoal(GoalEntity(GoalTypeEnum.WORK.number,GoalWorkisSelected,0))
        finish()
    }

    override fun onCancelButtonCLicked() {
        finish()
    }

}
