package com.example.myapplicatio.firebase

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplicatio.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.btn_choose.*
import kotlinx.android.synthetic.main.m_profile.*

class FUserProfile: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.m_profile, container, false)
    }

    private var mAuth: FirebaseAuth? =null
    private var mDate: FirebaseDatabase? =null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_m_enter.setOnClickListener {

        }
        btn_m_cancel.setOnClickListener {

        }
    }
}