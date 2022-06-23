package com.example.musthave.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.musthave.R

class Initial : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler().postDelayed({
            //val intent = Intent(this, MainActivity::class.java)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            //finish()
        }, 3000) // 3000 is the delayed time in milliseconds.
    }
}