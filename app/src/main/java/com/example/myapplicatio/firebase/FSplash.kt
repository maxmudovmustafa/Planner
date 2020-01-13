package com.example.myapplicatio.firebase

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.myapplicatio.R
import kotlinx.android.synthetic.main.m_splash.*


class FSplash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m_splash)

        btn_create.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.ll_container, FCreateAccount()).commit()
        }
        btn_login.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.ll_container, FLoginAccount()).commit()
        }
    }
}