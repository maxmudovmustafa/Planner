package com.example.myapplicatio.aralash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.myapplicatio.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        supportFragmentManager.beginTransaction()
                .add(R.id.ll_container, Splash(), "")
                .addToBackStack(null)
                .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}