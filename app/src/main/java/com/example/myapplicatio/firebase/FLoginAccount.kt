package com.example.myapplicatio.firebase

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplicatio.R
import com.example.myapplicatio.util.ToastUtils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.m_auth.*

class FLoginAccount : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.m_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(m_toolbar as Toolbar)
        (activity as AppCompatActivity).setTitle("Registret")
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }

    var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btn_m_enter.setOnClickListener {
            if (tv_m_name.text.toString().isEmpty()) {
                tv_m_name.error = "This must be filled"
                tv_m_name.requestFocus()
            } else {
                if (tv_m_pass.text.toString().isEmpty()) {
                    tv_m_pass.error = "This must be filled"
                    tv_m_pass.requestFocus()
                } else {
                    mAuth = FirebaseAuth.getInstance()
                    singIn(tv_m_name.text.toString(), tv_m_pass.text.toString())
                }
            }
        }
        btn_m_enter.text = "Sign UP"
        btn_m_cancel.setOnClickListener {
            activity!!.fragmentManager.popBackStack()
        }
    }

    override fun onStart() {
        super.onStart()
        var currentUser = mAuth?.currentUser
    }

    fun singIn(name: String, password: String) {
        mAuth?.signInWithEmailAndPassword(name, password)
                ?.addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth!!.currentUser
                        childFragmentManager.beginTransaction()
                                .replace(R.id.ll_container, FMain())
                                .commit()
                    } else {
                        ToastUtils.showShortToast(context, "Not Signed UP")
                    }
                }
    }
}