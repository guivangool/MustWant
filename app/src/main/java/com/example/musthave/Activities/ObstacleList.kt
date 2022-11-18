package com.example.musthave.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musthave.DataEntities.ObstacleStatus
import com.example.musthave.Factories.ObstacleListViewModelFactory
import com.example.musthave.Factories.ObstacleViewModelFactory
import com.example.musthave.MustWantApp
import com.example.musthave.ObstacleAdapter
import com.example.musthave.Repositories.ObstacleRepository
import com.example.musthave.viewModels.ObstacleListViewModel
import com.example.musthave.databinding.ActivityObstacleListBinding
import com.example.musthave.viewModels.ObstacleViewModel
import kotlinx.coroutines.launch

class ObstacleList : AppCompatActivity() {
    private var binding: ActivityObstacleListBinding? = null
    private lateinit var obstacleAdapter: ObstacleAdapter
    private lateinit var obstacleListViewModel: ObstacleListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding
        binding = ActivityObstacleListBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setRecyclerView()

        //DataBinding - ViewModel
        val obstacleDao = (application as MustWantApp).db.obstacleDao()
        val repository = ObstacleRepository(obstacleDao)
        val factory = ObstacleListViewModelFactory(repository)
        obstacleListViewModel = ViewModelProvider(this,factory).get(ObstacleListViewModel::class.java)

        //Observes ViewModel
        obstacleListViewModel.obstacleList.observe(this, Observer { obstacleList ->
            obstacleAdapter.submitList(obstacleList)
        })

        //Set back button to the Action Bar
        setSupportActionBar(binding?.tbObstacles)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.tbObstacles?.setNavigationOnClickListener {
            onBackPressed()
        }

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
}