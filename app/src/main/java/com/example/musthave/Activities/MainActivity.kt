package com.example.musthave.Activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
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
        observeSelectedGoals()

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

        //Register Progress button pressed
        binding?.tvProgress?.setOnClickListener {
            if (verifyGoalsSelected()) {
                hasToUpdate = true
                val intent = Intent(this@MainActivity, ProgressGoals::class.java)
                intent.putExtra("goalList", goalList)
                startActivity(intent)
            }
        }

        //Create inspiration button pressed
        binding?.tvAssignMotivation?.setOnClickListener {
            if (verifyGoalsSelected()) {
                val intent = Intent(this@MainActivity, CreateInspiration::class.java)
                intent.putExtra("goalList", goalList)
                startActivity(intent)
            }
        }

        //Remove Obstacle button pressed
        binding?.tvEmotion?.setOnClickListener {
            val intent = Intent(this@MainActivity, RemoveObstacle::class.java)
            startActivity(intent)
        }

        //Manage Obstacles button pressed
        binding?.tvManageObstacles?.setOnClickListener {
            val intent = Intent(this@MainActivity, ObstacleList::class.java)
            startActivity(intent)
        }

        //Start Again button pressed
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
                mainViewModel.deleteAll()
                customDialog.dismiss()
            }
            dialogBinding.btnNo.setOnClickListener {
                customDialog.dismiss()
            }
            customDialog.show()
        }

    private  fun showMessage(mainMessage: MainMessage) {
        when (mainMessage.messageNumber) {
            1 -> {
                binding?.ivToDo?.setImageDrawable(getResources().getDrawable(R.drawable.fondo_accion))
                binding?.tvToDO?.text = getResources().getString(R.string.action_goals_message)
                binding?.ivToDo?.scaleType = ImageView.ScaleType.FIT_START
            }
            2 -> {
                binding?.ivToDo?.setImageDrawable(getResources().getDrawable(R.drawable.fondo_accion))
                binding?.tvToDO?.text = getResources().getString(R.string.action_goals_progress_message)
                binding?.ivToDo?.scaleType = ImageView.ScaleType.FIT_START
            }
            3 -> {
                binding?.ivToDo?.setImageDrawable(getResources().getDrawable(R.drawable.fondo_recomendada))
                binding?.tvToDO?.text = getResources().getString(R.string.action_create_inspiration_message)
                binding?.ivToDo?.scaleType = ImageView.ScaleType.FIT_START
            }
            4 -> {
                var bitmap: Bitmap = BitmapFactory.decodeFile(mainMessage.image)
                binding?.ivToDo?.setImageBitmap(bitmap)
                binding?.tvToDO?.text = mainMessage.message
                binding?.ivToDo?.scaleType = ImageView.ScaleType.CENTER_CROP
                binding?.tvToDO?.setTextColor(resources.getColor(R.color.white))
            }
        }
    }

    //fun that obtains selected goals from the database
    private fun observeSelectedGoals() {
        //Observe ViewModel
        mainViewModel.goalsSelection.observe(this, androidx.lifecycle.Observer { selectedGoalsData ->
            goalsSelection = selectedGoalsData as ArrayList<GoalEntity>
            if (goalsSelection.size == 0)
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
            //TODO  - Remove goalList
            goalList.clear()
            for (goalSelected in goalsSelection )
            {
                goalList.add(goalSelected.goalId)
            }
        })
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
