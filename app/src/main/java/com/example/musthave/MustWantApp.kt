package com.example.musthave

import android.app.Application
import com.example.musthave.Database.MustWantDatabase

class MustWantApp : Application() {
    val db by lazy {
        MustWantDatabase.getInstance(this)
    }
}