package com.gvg.mustwant.Activities

import android.annotation.SuppressLint
import  android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gvg.mustwant.*
import com.gvg.mustwant.Adapters.MyGoalsAdapter
import com.gvg.mustwant.DataEntities.GoalEntity
import com.gvg.mustwant.DomainEntities.MainMessage
import com.gvg.mustwant.Enums.GoalTypeEnum
import com.gvg.mustwant.Factories.MainViewModelFactory
import com.gvg.mustwant.Fragments.ActionNeeded
import com.gvg.mustwant.GeneralFunctions.*
import com.gvg.mustwant.Repositories.MainRepository
import com.gvg.mustwant.viewModels.MainViewModel
import com.gvg.mustwant.databinding.ActivityMainBinding
import com.gvg.mustwant.databinding.CustomDialogAcceptCancelBinding
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), ActionNeeded.FunctionProvider{

    private var binding: ActivityMainBinding? = null
    private var goalAdapter: MyGoalsAdapter? = null
    private var hasToUpdate = false
    private var selectedGoal :Int? = 0
    private var goalList = ArrayList<Int>()
    private var goalsSelection = ArrayList<GoalEntity>()
    private lateinit var mainViewModel: MainViewModel

    override fun onBackPressed() {
        confirmExitApp()
    }

    override fun provideFunction(messageNumber:Int) {
        when (messageNumber) {
            1 -> binding?.tvSetGoalOption?.performClick()
            2 -> binding?.tvProgress?.performClick()
            3 -> binding?.tvAssignMotivation?.performClick()
            }
        }


    private fun confirmExitApp( ) {
        val customDialog = Dialog(this)
        val dialogBinding = CustomDialogAcceptCancelBinding.inflate(layoutInflater)

        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)

        dialogBinding.tvQuestion.text = this.getString(R.string.question_sure_want_exit)
        dialogBinding.tvExplanation.text = ""

        dialogBinding.btnYes.setOnClickListener {
            this.finish()
        }
        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //Animate options
        animateLogo(binding?.tvProgress,400)
        animateLogo(binding?.tvEmotion,400)
        animateLogo(binding?.tvManageObstacles,400)
        animateLogo(binding?.tvSetGoalOption,400)
        animateLogo(binding?.tvAssignMotivation,400)
        animateLogo(binding?.tvStartAgain,400)

        //Create ViewModel
        //Daos
        val configurationDao = (application as MustWantApp).db.configurationDao()
        val goalProgressDao = (application as MustWantApp).db.goalProgressDao()
        val inspirationDao = (application as MustWantApp).db.inspitationDao()
        val obstaclesDao = (application as MustWantApp).db.obstacleDao()

        //Repository
        val repository = MainRepository(configurationDao,goalProgressDao,inspirationDao,obstaclesDao)
        //Factory
        val factory = MainViewModelFactory(repository)
        //ViewModel
        mainViewModel =
                ViewModelProvider(this,factory).get(MainViewModel::class.java)

        //Observe View Model to show Selected Goals
        mainViewModel.goalsSelection.observe(this, Observer { selectedGoalsData ->
            setSelectedGoals(selectedGoalsData)
        })

        //Observe View Model to show messages
        mainViewModel.mainMessage.observe(this, Observer { mainMessage ->
            showMessage(mainMessage)
        })

        //User press "Select goals" button
        binding?.tvSetGoalOption?.setOnClickListener {
            hasToUpdate = true
            val intent = Intent(this@MainActivity, SelectGoals::class.java)
            startActivity(intent)
        }

        //User press "Register Progress" button
        binding?.tvProgress?.setOnClickListener {
            if (verifyGoalsSelected()) {
                hasToUpdate = true
                val intent = Intent(this@MainActivity, ProgressGoals::class.java)
                intent.putExtra("goalList", goalList)
                startActivity(intent)
            }
        }

        //User press "Create inspiration" button
        binding?.tvAssignMotivation?.setOnClickListener {
            if (verifyGoalsSelected()) {
                hasToUpdate = true
                val intent = Intent(this@MainActivity, CreateInspiration::class.java)
                intent.putExtra("goalList", goalList)
                startActivity(intent)
            }
        }

        //User press "Remove Obstacle" button
        binding?.tvEmotion?.setOnClickListener {
            val intent = Intent(this@MainActivity, RemoveObstacle::class.java)
            startActivity(intent)
        }

        //User press "Manage Obstacles" button
        binding?.tvManageObstacles?.setOnClickListener {
            val intent = Intent(this@MainActivity, ObstacleList::class.java)
            startActivity(intent)
        }


        //Observe View Model to Add amount of pending tasks to button name
        mainViewModel.amountPendingTasks.observe(this, Observer { amountPendingTasks ->
            var normalPart = ""
            var redPart = ""
            if (amountPendingTasks > 0)
            {
                normalPart = binding?.tvManageObstacles?.text.toString()
                redPart = amountPendingTasks.toString()
              binding?.tvManageObstacles?.text = binding?.tvManageObstacles?.text.toString() + " (" + amountPendingTasks.toString() + " )"
            }
        })

        mainViewModel.getAmountPendingTasks()

        //User press "Start Again" button
        binding?.tvStartAgain?.setOnClickListener {
            confirmDeleteAll(repository)
        }
    }
        //Ask user to confirm delete All
         private fun confirmDeleteAll( repository: MainRepository) {
            val customDialog = Dialog(this)
            val dialogBinding = CustomDialogAcceptCancelBinding.inflate(layoutInflater)

            customDialog.setContentView(dialogBinding.root)
            customDialog.setCanceledOnTouchOutside(false)

            dialogBinding.btnYes.setOnClickListener {
                //Force main screen update after deleting all the information
                hasToUpdate = true
                mainViewModel.deleteAll()
                customDialog.dismiss()
            }
            dialogBinding.btnNo.setOnClickListener {
                customDialog.dismiss()
            }
            customDialog.show()
        }

    @SuppressLint("UseCompatLoadingForDrawables")
    private  fun showMessage(mainMessage: MainMessage) {
        val bundle = Bundle()
        val fragment = ActionNeeded()

        fragment.setFunctionProvider(this)
        bundle.putInt("messageNumber",mainMessage.messageNumber)
        bundle.putString("message",mainMessage.message)
        bundle.putString("messageImage",mainMessage.image)
        fragment.arguments = bundle
        var fragmentTransaction  = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out)
        fragmentTransaction.replace(R.id.fragment_action_needed,fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        //animate recommended action button
//        binding ?.tvSetGoalOption!!.background = getDrawable (R.drawable.bg_option_main)
//        binding ?.tvProgress!!.background = getDrawable (R.drawable.bg_option_main)
//        binding ?.tvAssignMotivation!!.background = getDrawable (R.drawable.bg_option_main)
//        binding?.tvSetGoalOption!!.setTextColor(getResources().getColor(R.color.black))
//        binding?.tvProgress!!.setTextColor(getResources().getColor(R.color.black))
//        binding?.tvAssignMotivation!!.setTextColor(getResources().getColor(R.color.black))

        when (mainMessage.messageNumber) {
            1 -> {

                //vibrateView(binding?.tvSetGoalOption)
//                binding?.tvSetGoalOption!!.background = getDrawable (R.drawable.bg_recomended_option_main)
//                binding?.tvSetGoalOption!!.setTextColor(getResources().getColor(R.color.black))
        }
            2 ->{
                //vibrateView(binding?.tvProgress)
//                binding?.tvProgress!!.background = getDrawable (R.drawable.bg_recomended_option_main)
//                binding?.tvProgress!!.setTextColor(getResources().getColor(R.color.black))
            }
            3 ->{
                //vibrateView(binding?.tvAssignMotivation)
//                binding?.tvAssignMotivation!!.background = getDrawable (R.drawable.bg_recomended_option_main)
//                binding?.tvAssignMotivation!!.setTextColor(getResources().getColor(R.color.black))
            }
        }

    }

    //Update UI with selected goals
    private fun setSelectedGoals(selectedGoalsData: List<GoalEntity>) {

            goalsSelection = selectedGoalsData as ArrayList<GoalEntity>
            if (goalsSelection.isEmpty())
            {
                //Hide Goals List - Show Message
                binding?.rvMyGoals?.visibility = View.GONE
                binding?.tvGoalsNotSelected?.visibility = View.VISIBLE
            }
            else
            {
                //Show Goals List - Hide message
                binding?.rvMyGoals?.visibility = View.VISIBLE
                binding?.tvGoalsNotSelected?.visibility = View.GONE
            }

            setupMyGoalsRecyclerView(selectedGoalsData)

            goalAdapter?.setOnClickListener(object : MyGoalsAdapter.OnClickListener {
            override fun onCLick(goal: String) {
                selectedGoal = GoalTypeEnum.values().find {it.getText(this@MainActivity) == goal}?.number

                val intent = Intent(this@MainActivity, ProgressGoalsList::class.java)
                intent.putExtra("selectedGoal", selectedGoal)
                startActivity(intent)
            }
        })
            //Update list of IDs of selected goals
            goalList = getGoalIDs(selectedGoalsData)
    }

    //Return ArrayList<Int> with all the IDs of selected goals
    private fun getGoalIDs(goalsSelection:ArrayList<GoalEntity>):ArrayList<Int>{
        val goalListInts = ArrayList<Int>()
        for (goalSelected in goalsSelection )
        {
            goalListInts.add(goalSelected.goalId)
        }
        return goalListInts
    }

    //fun that set the data source to the list of goals (recycler VIew)
    private fun setupMyGoalsRecyclerView(selectedGoals: ArrayList<GoalEntity>) {
            binding?.rvMyGoals?.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            goalAdapter = MyGoalsAdapter(this,selectedGoals)
            binding?.rvMyGoals?.adapter = goalAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    //Override fun to update the activity after selecting goals
    override fun onResume() {
        super.onResume()
        if (hasToUpdate) {
            mainViewModel.getSelectedGoals()
            hasToUpdate = false
        }
    }

    private fun verifyGoalsSelected(): Boolean {
        if (goalsSelection.isEmpty()) {
            Toast.makeText(this, getString(R.string.messageNoGoalSelected), Toast.LENGTH_LONG)
                .show()
            return false
        } else {
            return true
        }
    }
}
