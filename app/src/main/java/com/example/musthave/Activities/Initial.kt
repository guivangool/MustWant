package com.example.musthave.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.musthave.databinding.ActivityInitialBinding
import com.example.musthave.GeneralFunctions.*

class Initial : AppCompatActivity() {
    private var binding: ActivityInitialBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val eventBundle = Bundle()
        super.onCreate(savedInstanceState)

        //Binding
        binding = ActivityInitialBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //Animate logo
        animateLogo(binding?.ivLogo)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        },1500)
    }
}