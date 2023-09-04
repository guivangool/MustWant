package com.example.musthave.Activities


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import com.example.musthave.databinding.ActivityInitialBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.example.musthave.GeneralFunctions.*

class Initial : AppCompatActivity() {
    private lateinit var analytics: FirebaseAnalytics
    private var binding: ActivityInitialBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val eventBundle = Bundle()
        super.onCreate(savedInstanceState)

        //Binding
        binding = ActivityInitialBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //Animate logo
        animateLogo(binding?.ivLogo)

        analytics = Firebase.analytics
        eventBundle.putString(FirebaseAnalytics.Param.LOCATION,"MDQ")
        analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN,eventBundle)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        },1500)
    }
}