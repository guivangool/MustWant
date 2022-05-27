package com.example.musthave.Activities

import android.os.Bundle
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isNotEmpty
import androidx.lifecycle.lifecycleScope
import com.example.musthave.DataEntities.ObstacleEntity
import com.example.musthave.MustWantApp
import com.example.musthave.databinding.ActivityRemoveObstacleBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RemoveObstacle : AppCompatActivity() {
    var binding: ActivityRemoveObstacleBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding
        binding = ActivityRemoveObstacleBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //Set back button to the Acion Bar
        setSupportActionBar(binding?.tbRemoveObstacle)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.tbRemoveObstacle?.setNavigationOnClickListener {
            onBackPressed()
        }

        //Set minium to the DatePicker
        binding?.dpObstacle?.setMinDate(System.currentTimeMillis())

        binding?.btnCancel?.setOnClickListener {
            finish()
        }
        binding?.btnAccept?.setOnClickListener {
            var datePicker: DatePicker? = binding?.dpObstacle

            if (binding!!.etSetObstacleName?.text!!.isNotEmpty()
                && datePicker!!.isNotEmpty()
            ) {
                var year = datePicker.year
                var month = datePicker.month
                var day = datePicker.dayOfMonth

                var calendar = Calendar.getInstance()
                calendar.set(year, month, day)

                var date = SimpleDateFormat(
                    "yyyyMMdd",
                    Locale.getDefault()
                ).format(calendar.time)

                lifecycleScope.launch {
                    val obstacleDao = (application as MustWantApp).db.obstacleDao()
                    obstacleDao.insert(

                        ObstacleEntity(
                            null,
                            binding!!.etSetObstacleName?.text.toString(),
                            date,
                            calendar.time,
                        0)
                        )
                }
            }
            finish()
        }
    }
}