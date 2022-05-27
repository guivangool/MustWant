package com.example.musthave.Activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musthave.*
import com.example.musthave.Adapters.MyGoalsAdapter
import com.example.musthave.DataEntities.GoalProgressEntity
import com.example.musthave.DomainEntities.MyGoal
import com.example.musthave.databinding.ActivityMainBinding
import com.example.musthave.databinding.CustomDialogAcceptCancelBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var goalAdapter: MyGoalsAdapter? = null
    private var hasToUpdate = false
    private var selectedGoal = 0

    var goalList = ArrayList<Int>()
    var myGoalList = ArrayList<MyGoal>()
    var isNew = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //Load Selected Goals
        loadSelectedGoals()

        //Select goals button pressed
        binding?.tvSetGoalOption?.setOnClickListener {
            goToSelectGoals()
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
            intent.putExtra("goalList", goalList)
            startActivity(intent)
        }

        //Manage Obstacles button pressed
        binding?.tvManageObstacles?.setOnClickListener {
            val intent = Intent(this@MainActivity, ObstacleList::class.java)
            startActivity(intent)
        }

        //Start Again button pressed
        binding?.tvStartAgain?.setOnClickListener {
            confirmDeleteAll()
        }
    }

    private fun confirmDeleteAll() {
        val customDialog = Dialog(this)
        val dialogBinding = CustomDialogAcceptCancelBinding.inflate(layoutInflater)

        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)

        dialogBinding.btnYes.setOnClickListener {
            val configurationDao = (application as MustWantApp).db.configurationDao()
            lifecycleScope.launch {
                configurationDao.deleteAllDatabaseData()
                loadSelectedGoals()
                customDialog.dismiss()
            }

        }
        dialogBinding.btnNo.setOnClickListener{
            customDialog.dismiss()
        }
        customDialog.show()

    }

    private fun getYesterdayDate(): Date {
        var calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        return calendar.time
    }
    private fun rand(from: Int, to: Int) : Int {
        return Random.nextInt(to - from) + from
    }
    private fun wasThereProgress() {
        val goalProgressDao = (application as MustWantApp).db.goalProgressDao()
        lifecycleScope.launch {
            var progress = goalProgressDao.getAllFromYesterday(getYesterdayDate())
            if (progress == 0) {
                showMessage(2, getResources().getString(R.string.action_goals_progress_message))
            } else {
                val inspirationDao = (application as MustWantApp).db.inspitationDao()
                lifecycleScope.launch {
                    inspirationDao.getAllGoalInspiration().collect {
                        if (it != null && it.size > 0) {
                            var random = 0
                            if (it.size > 1)
                            {
                                random = rand(0,it.size)
                            }
                            var bitmap: Bitmap = BitmapFactory.decodeFile(it[random].image)
                            binding?.ivToDo?.setImageBitmap(bitmap)
                            showMessage(4,it[random].phrase.toString())
                        } else  {
                            showMessage(3,getResources().getString(R.string.action_create_inspiration_message))
                        }
                    }
                }
            }
        }
    }

    private  fun showMessage(idMessage: Int, message:String) {
        when (idMessage) {
            1 -> {
                binding?.ivToDo?.setImageDrawable(getResources().getDrawable(R.drawable.fondo_accion))
                binding?.tvToDO?.text = message
                binding?.ivToDo?.scaleType = ImageView.ScaleType.FIT_START
            }
            2 -> {
                binding?.ivToDo?.setImageDrawable(getResources().getDrawable(R.drawable.fondo_accion))
                binding?.tvToDO?.text = message
                binding?.ivToDo?.scaleType = ImageView.ScaleType.FIT_START
            }
            3 -> {
                binding?.ivToDo?.setImageDrawable(getResources().getDrawable(R.drawable.fondo_recomendada))
                binding?.tvToDO?.text = message
                binding?.ivToDo?.scaleType = ImageView.ScaleType.FIT_START
            }
            4 -> {
                binding?.tvToDO?.text = message
                binding?.ivToDo?.scaleType = ImageView.ScaleType.CENTER_CROP
                binding?.tvToDO?.setTextColor(resources.getColor(R.color.white))
            }
        }
    }

    private fun goToSelectGoals() {
        hasToUpdate = true
        val intent = Intent(this@MainActivity, SelectGoals::class.java)
        intent.putExtra("goalList", goalList)
        intent.putExtra("isNew", isNew)
        startActivity(intent)
    }

    //fun that obtains selected goals from the database
    private fun loadSelectedGoals() {
        val configurationDao = (application as MustWantApp).db.configurationDao()
        //Load selected goals from database
        lifecycleScope.launch {
            configurationDao.getConfiguration(1).collect {
                goalList.clear()
                myGoalList.clear()

                if (it != null) {
                    isNew = false

                    if (it.goalMe) {
                        goalList.add(1)
                        myGoalList.add(MyGoal(1, 0))
                    }
                    if (it.goalHome) {
                        goalList.add(2)
                        myGoalList.add(MyGoal(2, 0))
                    }
                    if (it.goalWork) {
                        goalList.add(3)
                        myGoalList.add(MyGoal(3, 0))
                    }
                    if (it.goalRelation) {
                        goalList.add(4)
                        myGoalList.add(MyGoal(4, 0))
                    }
                } else {
                    isNew = true
                }

                if (goalList.isNotEmpty())
                {
                    //Show Goals List - Hide message
                    binding?.rvMyGoals?.visibility = View.VISIBLE
                    binding?.tvGoalsNotSelected?.visibility = View.GONE
                }
                else
                {
                    //Hide Goals List - Show Message
                    binding?.rvMyGoals?.visibility = View.GONE
                    binding?.tvGoalsNotSelected?.visibility = View.VISIBLE
                    showMessage(1,getResources().getString(R.string.action_goals_message))
                }
                setTotals(myGoalList)
                setupMyGoalsRecyclerView()
                if (!goalList.isEmpty()) wasThereProgress()
                goalAdapter?.setOnClickListener(object : MyGoalsAdapter.OnClickListener {
                    override fun onCLick(goal: String) {
                        when (goal) {
                            "Yo" -> {
                                selectedGoal = 1
                            }
                            "Hogar" -> {
                                selectedGoal = 2
                            }
                            "Trabajo" -> {
                                selectedGoal = 3
                            }
                            "Relaciones" -> {
                                selectedGoal = 4
                            }
                        }
                        val intent = Intent(this@MainActivity, ProgressGoalsList::class.java)
                        intent.putExtra("selectedGoal", selectedGoal)
                        startActivity(intent)
                    }
                })

            }
        }
    }

    //fun that set the data source to the list of goals (recycler VIew)
    private fun setupMyGoalsRecyclerView() {
        if (binding?.rvMyGoals?.layoutManager == null) {
            binding?.rvMyGoals?.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            goalAdapter = MyGoalsAdapter(myGoalList)
            binding?.rvMyGoals?.adapter = goalAdapter
        }
    }

    fun setTotals(myGoalList: ArrayList<MyGoal>) {
        val goalProgressDao = (application as MustWantApp).db.goalProgressDao()
        var totalsProgress: ArrayList<GoalProgressEntity>
        var percentajeMe = 0
        var percentajeHome = 0
        var percentajeWork = 0
        var percentajeRelations = 0
        var substract = 0

        lifecycleScope.launch {
            goalProgressDao.getAllTotalProgress().collect {
                if (it != null) {
                    totalsProgress = it as ArrayList<GoalProgressEntity>
                }
                for (goalProgressTotal in it) {
                    if (goalProgressTotal.totalProgress.compareTo(0) > 0)
                        substract = 1
                    else if (goalProgressTotal.totalProgress.compareTo(0) < 0)
                        substract = -1
                    else
                        substract = 0

                    when (goalProgressTotal.goalID) {
                        1 -> percentajeMe = percentajeMe + substract
                        2 -> percentajeHome = percentajeHome + substract
                        3 -> percentajeWork = percentajeWork + substract
                        4 -> percentajeRelations = percentajeRelations + substract
                    }
                }
                if (percentajeMe < 0) percentajeMe = 0
                if (percentajeHome < 0) percentajeHome = 0
                if (percentajeWork < 0) percentajeWork = 0
                if (percentajeRelations < 0) percentajeRelations = 0


                for (myGoal in myGoalList) {
                    when (myGoal.goalId) {
                        1 -> myGoal.goalPercentaje = percentajeMe
                        2 -> myGoal.goalPercentaje = percentajeHome
                        3 -> myGoal.goalPercentaje = percentajeWork
                        4 -> myGoal.goalPercentaje = percentajeRelations
                    }
                }
                goalAdapter!!.notifyDataSetChanged()
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    //Override fun to update the activity after selecting goals
    override fun onResume() {
        super.onResume()
        if (hasToUpdate) {
            loadSelectedGoals()
            goalAdapter!!.notifyDataSetChanged()
        }
        hasToUpdate = false
    }

    private fun verifyGoalsSelected(): Boolean {
        if (goalList.isEmpty()) {
            Toast.makeText(this, getString(R.string.messageNoGoalSelected), Toast.LENGTH_LONG)
                .show()
            return false
        } else {
            return true
        }
    }
}
