package com.example.myapplicatio.z_camera

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.myapplicatio.R
import kotlinx.android.synthetic.main.choose.*

class Chooose: Activity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choose)
        btn1.setOnClickListener {
            var intent = Intent(this, VideoStream::class.java)
            startActivity(intent)
        }
        btn2.setOnClickListener {
            var intent = Intent(this, VideoPlayBack::class.java)
            startActivity(intent)
        }
        btn3.setOnClickListener {
            var intent = Intent(this, IPCamera::class.java)
            startActivity(intent)
        }
    }
}