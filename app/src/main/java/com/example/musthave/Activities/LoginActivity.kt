package com.example.musthave.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.musthave.R
import com.example.musthave.databinding.ActivityLoginBinding
import com.example.musthave.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {
    private var binding: ActivityLoginBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnlogin?.setBackgroundResource(R.drawable.shape_button_rounded)

    }
}