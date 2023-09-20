package com.gvg.mustwant

import android.app.Application
import com.gvg.mustwant.Database.MustWantDatabase

class MustWantApp : Application() {
    val db by lazy {
        MustWantDatabase.getInstance(this)
    }
}