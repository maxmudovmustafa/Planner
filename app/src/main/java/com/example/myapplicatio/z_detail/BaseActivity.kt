package com.example.myapplicatio.z_detail

import android.app.Fragment
import android.app.FragmentManager
import android.support.v7.app.AppCompatActivity

open class BaseActivity : AppCompatActivity(){
    companion object {
        fun addFragmentToActivity(manager: FragmentManager,
                                  fragment: Fragment,
                                  frameId: Int,
                                  tag: String){
            val beginTransaction = manager.beginTransaction()
            beginTransaction.replace(frameId, fragment, tag)
            beginTransaction.commit()
        }

    }
}