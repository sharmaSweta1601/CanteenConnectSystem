package com.example.myapplication

import android.app.Application
import com.cloudinary.android.MediaManager

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val config = hashMapOf(
            "cloud_name" to "dhf9xry5q",
            "api_key" to "421314716285649",
            "api_secret" to "pp1CHTE-RY1KToOsHuSN5GreJV0"
        )

        MediaManager.init(this, config)
    }
}
