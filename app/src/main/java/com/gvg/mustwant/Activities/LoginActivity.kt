package com.gvg.mustwant.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gvg.mustwant.R
import com.gvg.mustwant.databinding.ActivityLoginBinding

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