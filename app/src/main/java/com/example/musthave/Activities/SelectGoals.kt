package com.example.musthave.Activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import com.example.musthave.DataEntities.GoalEntity
import com.example.musthave.Enums.GoalTypeEnum
import com.example.musthave.Factories.SelectGoalsViewModelFactory
import com.example.musthave.Fragments.AcceptCancel
import com.example.musthave.Fragments.Spinner
import com.example.musthave.GeneralFunctions.animateLogo
import com.example.musthave.Interfaces.OnAcceptCancelButtonClickListener
import com.example.musthave.Interfaces.OnSpinnerButtonCLickListener
import com.example.musthave.MustWantApp
import com.example.musthave.R
import com.example.musthave.Repositories.SelectGoalsRepository
import com.example.musthave.databinding.ActivitySelectGoalsBinding
import com.example.musthave.viewModels.SelectGoalsViewModel


class SelectGoals : AppCompatActivity(), OnAcceptCancelButtonClickListener,OnSpinnerButtonCLickListener {

    private var binding: ActivitySelectGoalsBinding? = null
    private var GoalMeisSelected = false
    private var GoalHomeisSelected = false
    private var GoalRelationisSelected = false
    private var GoalWorkisSelected = false
    private lateinit var selectGoalsViewModel: SelectGoalsViewModel
    private var goals = ArrayList<GoalEntity>()
    private var fragmentSpinnerMe : Spinner? = null
    private var fragmentSpinnerHome : Spinner? = null
    private var fragmentSpinnerWork : Spinner? = null
    private var fragmentSpinnerRelations : Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding
        binding = ActivitySelectGoalsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //Animate options
        animateLogo(binding?.GoalMe,400)
        animateLogo(binding?.GoalHome,400)
        animateLogo(binding?.GoalWork,400)
        animateLogo(binding?.GoalRelations,400)

        //Action Bar
        //setSupportActionBar(binding?.tbSelectGoals)

        //if (supportActionBar != null) {
         //   supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //}
        //binding?.tbSelectGoals?.setNavigationOnClickListener {
         //   onBackPressed()
        //}

        //Add the fragment AcceptCancel programmatically
        if (savedInstanceState == null) {
            val fragment = AcceptCancel()
            val fragmentTransaction  = supportFragmentManager.beginTransaction()
            fragment.setOnAcceptCancelButtonClickListener(this)
            fragmentTransaction.add(R.id.fragment_accept_cancel,fragment)
            fragmentTransaction.commit()
        }

        //Add spinners (#days)
        if (savedInstanceState == null) {
            fragmentSpinnerMe = Spinner.newInstance("SpinnerMe")
            fragmentSpinnerHome = Spinner.newInstance("SpinnerHome")
            fragmentSpinnerWork = Spinner.newInstance("SpinnerWork")
            fragmentSpinnerRelations = Spinner.newInstance("SpinnerRelations")
            val fragmentTransactionSpinner  = supportFragmentManager.beginTransaction()

            fragmentSpinnerMe?.setOnSpinnerButtonCLickListener(this)
            fragmentSpinnerHome?.setOnSpinnerButtonCLickListener(this)
            fragmentSpinnerWork?.setOnSpinnerButtonCLickListener(this)
            fragmentSpinnerRelations?.setOnSpinnerButtonCLickListener(this)

            fragmentTransactionSpinner.add(R.id.fragment_GoalMe_Days,fragmentSpinnerMe!!)
            fragmentTransactionSpinner.add(R.id.fragment_GoalHome_Days, fragmentSpinnerHome!!)
            fragmentTransactionSpinner.add(R.id.fragment_GoalWork_Days,fragmentSpinnerWork!!)
            fragmentTransactionSpinner.add(R.id.fragment_GoalRelations_Days,fragmentSpinnerRelations!!)

            fragmentTransactionSpinner.commit()
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
                var fragment : FragmentContainerView? = null
                for (goal in goals) {
                    when (goal.goalId) {
                        GoalTypeEnum.ME.number -> {
                            selectedGoal( binding?.GoalMe as TextView, goal.selected)
                            GoalMeisSelected = goal.selected
                            fragment = binding?.fragmentGoalMeDays
                            fragmentSpinnerMe!!.requireView().findViewById<EditText>(R.id.etDays).setText(goal.goalDays.toString())
                        }
                        GoalTypeEnum.HOME.number -> {
                            selectedGoal(binding?.GoalHome as TextView, goal.selected)
                            GoalHomeisSelected = goal.selected
                            fragment = binding?.fragmentGoalHomeDays
                            fragmentSpinnerHome!!.requireView().findViewById<EditText>(R.id.etDays).setText(goal.goalDays.toString())
                        }
                        GoalTypeEnum.WORK.number -> {
                            selectedGoal(binding?.GoalWork as TextView, goal.selected)
                            GoalWorkisSelected = goal.selected
                            fragment = binding?.fragmentGoalWorkDays
                            fragmentSpinnerWork!!.requireView().findViewById<EditText>(R.id.etDays).setText(goal.goalDays.toString())
                        }
                        GoalTypeEnum.RELATIONS.number -> {
                            selectedGoal(binding?.GoalRelations as TextView, goal.selected)
                            GoalRelationisSelected = goal.selected
                            fragment = binding?.fragmentGoalRelationsDays
                            fragmentSpinnerRelations!!.requireView().findViewById<EditText>(R.id.etDays).setText(goal.goalDays.toString())
                        }
                    }
                    if (fragment != null && goal.selected)
                        fragment!!.visibility = View.VISIBLE
                    else
                        fragment!!.visibility = View.GONE
                }

            })

        binding?.GoalMe?.setOnClickListener {
            GoalMeisSelected = !GoalMeisSelected
            selectedGoal(binding?.GoalMe as TextView, GoalMeisSelected)
            if (GoalMeisSelected)
                binding?.fragmentGoalMeDays!!.visibility = View.VISIBLE
            else
                binding?.fragmentGoalMeDays!!.visibility = View.GONE
        }
        binding?.GoalHome?.setOnClickListener {
            GoalHomeisSelected = !GoalHomeisSelected
            selectedGoal(binding?.GoalHome as TextView, GoalHomeisSelected)
            if (GoalHomeisSelected)
                binding?.fragmentGoalHomeDays!!.visibility = View.VISIBLE
            else
                binding?.fragmentGoalHomeDays!!.visibility = View.GONE
        }
        binding?.GoalRelations?.setOnClickListener {
            GoalRelationisSelected = !GoalRelationisSelected
            selectedGoal(binding?.GoalRelations as TextView, GoalRelationisSelected)
            if (GoalRelationisSelected)
                binding?.fragmentGoalRelationsDays!!.visibility = View.VISIBLE
            else
                binding?.fragmentGoalRelationsDays!!.visibility = View.GONE
        }
        binding?.GoalWork?.setOnClickListener {
            GoalWorkisSelected = !GoalWorkisSelected
            selectedGoal(binding?.GoalWork as TextView, GoalWorkisSelected)
            if (GoalWorkisSelected)
                binding?.fragmentGoalWorkDays!!.visibility = View.VISIBLE
            else
                binding?.fragmentGoalWorkDays!!.visibility = View.GONE
        }

    }

    private fun selectedGoal( tv: TextView, selected: Boolean) {
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

        selectGoalsViewModel.updateGoal(GoalEntity(GoalTypeEnum.ME.number,GoalMeisSelected,0,fragmentSpinnerMe!!.requireView().findViewById<EditText>(R.id.etDays).text.toString().toInt()))
        selectGoalsViewModel.updateGoal(GoalEntity(GoalTypeEnum.HOME.number,GoalHomeisSelected,0,fragmentSpinnerHome!!.requireView().findViewById<EditText>(R.id.etDays).text.toString().toInt()))
        selectGoalsViewModel.updateGoal(GoalEntity(GoalTypeEnum.RELATIONS.number,GoalRelationisSelected,0,fragmentSpinnerRelations!!.requireView().findViewById<EditText>(R.id.etDays).text.toString().toInt()))
        selectGoalsViewModel.updateGoal(GoalEntity(GoalTypeEnum.WORK.number,GoalWorkisSelected,0,fragmentSpinnerWork!!.requireView().findViewById<EditText>(R.id.etDays).text.toString().toInt()))
        finish()
    }

    override fun onCancelButtonCLicked() {
        finish()
    }

    override fun onIncrementButtonCLicked(fragmentId:String?) {
        Log.d("INCREMENTAR",fragmentId.toString())
    }

    override fun onDecrementButtonCLicked(fragmentId:String?) {
        Log.d("DECREMENTAR",fragmentId.toString())
    }

}
