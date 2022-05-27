package com.example.musthave.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musthave.DataEntities.ObstacleEntity
import com.example.musthave.DataEntities.ObstacleStatus
import com.example.musthave.GoalProgressAdapter
import com.example.musthave.MustWantApp
import com.example.musthave.ObstacleAdapter
import com.example.musthave.databinding.ActivityObstacleListBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ObstacleList : AppCompatActivity() {
    private var binding: ActivityObstacleListBinding? = null
    private lateinit var obstacleAdapter: ObstacleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding
        binding = ActivityObstacleListBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //Set back button to the Acion Bar
        setSupportActionBar(binding?.tbObstacles)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.tbObstacles?.setNavigationOnClickListener {
            onBackPressed()
        }

        setRecyclerView()
        loadObstacles()

        obstacleAdapter.setOnClickListener(object : ObstacleAdapter.OnClickListener {
            override fun onCLick(id: Int,status:Int) {
                lifecycleScope.launch {
                    val obstacleDao = (application as MustWantApp).db.obstacleDao()
                    obstacleDao.updateStatus(
                        ObstacleStatus(id, status)
                    )
                }
            }
        })
    }

    private fun setRecyclerView() {
        binding?.rvObstacles?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        obstacleAdapter = ObstacleAdapter()
        binding?.rvObstacles?.adapter = obstacleAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun loadObstacles() {
        var obstaclesList = ArrayList<ObstacleEntity>()

        val obstacleDao = (application as MustWantApp).db.obstacleDao()
        //Load obstacles from database
        lifecycleScope.launch {
            obstacleDao.getAll().collect {
                if (it != null) {
                    obstaclesList = it as ArrayList<ObstacleEntity>
                    obstacleAdapter.submitList(obstaclesList)
                }
            }

        }
    }
}