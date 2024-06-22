package com.example.ecometer.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ecometer.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Simulate loading time
        val splashTimeOut: Long = 3000 // 3 seconds
        val homeIntent = Intent(this, MainActivity::class.java)

        // Delay to show splash screen
        window.decorView.postDelayed({
            startActivity(homeIntent)
            finish()
        }, splashTimeOut)
    }
}
