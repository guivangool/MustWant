package com.example.musthave.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import com.example.musthave.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class Initial : AppCompatActivity() {
    private lateinit var analytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        val eventBundle = Bundle()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)

        analytics = Firebase.analytics
        eventBundle.putString(FirebaseAnalytics.Param.LOCATION,"MDQ")
        analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN,eventBundle)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        },2000)

    }
}