package com.example.myapplicatio.firebase

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplicatio.R
import com.example.myapplicatio.util.ToastUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.m_account_create.*

class FCreateAccount : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.m_account_create, container, false)
    }

    private var mAuth: FirebaseAuth? = null
    private var mDate: FirebaseDatabase? = null
    private var dialog: ProgressDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        btn_m_enter.setOnClickListener {

            signUp(tv_m_log.text.toString(), tv_m_pass.text.toString())
        }
        btn_m_cancel.setOnClickListener {
            ToastUtils.showShortToast(context, "Exit")
        }
    }

    fun signUp(name: String, password: String) {
        dialog = ProgressDialog.show(context, "Registrtation", "Please wait", true, false)
        mAuth!!.createUserWithEmailAndPassword(name, password)
                .addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {

                        var cuser = FirebaseAuth.getInstance().currentUser
                        var id = cuser!!.uid

                        val child = FirebaseDatabase.getInstance().reference.child("pictures").child(id)
                        var map = HashMap<String, String>()
                        map["name"] = tv_m_log.text.toString()
                        map["login"] = tv_m_name.text.toString()
                        map["password"] = tv_m_pass.text.toString()
                        map["image"] = "default"
                        map["thumb_nail"] = "default"

                        child.setValue(map).addOnCompleteListener(activity!!) { task ->
                                    if (task.isSuccessful) {
                                        dialog!!.dismiss()
                                        val user = mAuth!!.currentUser
                                        childFragmentManager.beginTransaction()
                                                .replace(R.id.ll_container, FMain())
                                                .commit()
                                    }
                                }

                    } else {
                        dialog!!.hide()
                        ToastUtils.showShortToast(context, "Not Signed UP")
                    }
                }
    }
}