package com.gvg.mustwant.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gvg.mustwant.DataEntities.ObstacleStatus
import com.gvg.mustwant.Factories.ObstacleListViewModelFactory
import com.gvg.mustwant.MustWantApp
import com.gvg.mustwant.ObstacleAdapter
import com.gvg.mustwant.Repositories.ObstacleRepository
import com.gvg.mustwant.viewModels.ObstacleListViewModel
import com.gvg.mustwant.databinding.ActivityObstacleListBinding

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
                obstacleListViewModel.updateStatus(ObstacleStatus(id, status))
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