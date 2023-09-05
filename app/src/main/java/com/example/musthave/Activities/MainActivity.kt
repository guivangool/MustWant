package com.example.musthave.Activities

import android.annotation.SuppressLint
import  android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musthave.*
import com.example.musthave.Adapters.MyGoalsAdapter
import com.example.musthave.DataEntities.GoalEntity
import com.example.musthave.DomainEntities.MainMessage
import com.example.musthave.Enums.GoalTypeEnum
import com.example.musthave.Factories.MainViewModelFactory
import com.example.musthave.Fragments.ActionNeeded
import com.example.musthave.GeneralFunctions.*
import com.example.musthave.Repositories.MainRepository
import com.example.musthave.viewModels.MainViewModel
import com.example.musthave.databinding.ActivityMainBinding
import com.example.musthave.databinding.CustomDialogAcceptCancelBinding
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var goalAdapter: MyGoalsAdapter? = null
    private var hasToUpdate = false
    private var selectedGoal :Int? = 0
    private var goalList = ArrayList<Int>()
    private var goalsSelection = ArrayList<GoalEntity>()
    private lateinit var mainViewModel: MainViewModel
    
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
        //Repository
        val repository = MainRepository(configurationDao,goalProgressDao,inspirationDao)
        //Factory
        val factory = MainViewModelFactory(repository)
        //ViewModel
        mainViewModel =
                ViewModelProvider(this,factory).get(MainViewModel::class.java)

        //Observe View Model to show Selected Goals
        mainViewModel.goalsSelection.observe(this, androidx.lifecycle.Observer { selectedGoalsData ->
            setSelectedGoals(selectedGoalsData)
        })

        //Observe View Model to show messages
        mainViewModel.mainMessage.observe(this, androidx.lifecycle.Observer { mainMessage ->
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
        binding ?.tvSetGoalOption!!.background = getDrawable (R.drawable.bg_option_main)
        binding ?.tvProgress!!.background = getDrawable (R.drawable.bg_option_main)
        binding ?.tvAssignMotivation!!.background = getDrawable (R.drawable.bg_option_main)
        binding?.tvSetGoalOption!!.setTextColor(getResources().getColor(R.color.white))
        binding?.tvProgress!!.setTextColor(getResources().getColor(R.color.white))
        binding?.tvAssignMotivation!!.setTextColor(getResources().getColor(R.color.white))

        when (mainMessage.messageNumber) {
            1 -> {
                vibrateView(binding?.tvSetGoalOption)
                binding?.tvSetGoalOption!!.background = getDrawable (R.drawable.bg_recomended_option_main)
                binding?.tvSetGoalOption!!.setTextColor(getResources().getColor(R.color.black))
        }
            2 ->{
                vibrateView(binding?.tvProgress)
                binding?.tvProgress!!.background = getDrawable (R.drawable.bg_recomended_option_main)
                binding?.tvProgress!!.setTextColor(getResources().getColor(R.color.black))
            }
            3 ->{
                vibrateView(binding?.tvAssignMotivation)
                binding?.tvAssignMotivation!!.background = getDrawable (R.drawable.bg_recomended_option_main)
                binding?.tvAssignMotivation!!.setTextColor(getResources().getColor(R.color.black))
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
                selectedGoal = GoalTypeEnum.values().find {it.label == goal}?.number

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
            goalAdapter = MyGoalsAdapter(selectedGoals)
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
