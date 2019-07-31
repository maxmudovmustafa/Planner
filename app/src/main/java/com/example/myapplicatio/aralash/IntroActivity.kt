package com.example.myapplicatio.aralash

import android.app.ProgressDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import com.example.myapplicatio.R
import com.example.myapplicatio.dashboard.SessionIndexFragment
import com.example.myapplicatio.db.Data
import com.example.myapplicatio.db.UserEntity
import com.example.myapplicatio.db.UserModelView
import kotlinx.android.synthetic.main.into_activity.*
import uz.greenwhite.lib.mold.Mold
import uz.greenwhite.lib.view_setup.DialogBuilder
import uz.greenwhite.lib.view_setup.UI
import uz.greenwhite.lib.view_setup.ViewSetup


class IntroActivity : AppCompatActivity() {

    private var viewModel2: UserModelView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.into_activity)
        var vs = ViewSetup(layoutInflater.inflate(R.layout.dialog_registration, null, false))

        val factory2 = UserViewFactory(App.getApplicationContext(this))
        viewModel2 = ViewModelProviders.of(this, factory2).get(UserModelView::class.java)

        ll_log_in.setOnClickListener {
            getInfo(vs)
        }

        ll_sign_in.setOnClickListener {
            getInfo(vs)
        }

        vs.button(R.id.btn_cancel).setOnClickListener {
            gotoActivity()
        }

        vs.button(R.id.btn_registr).setOnClickListener {
            val name = vs.editText(R.id.ed_user_name).text.toString()
            val passw = vs.editText(R.id.ed_user_passw).text.toString()
            val detail = vs.editText(R.id.ed_user_detail).text.toString()
            if (passw.isEmpty() || passw.length == 4) {
                val userEntity = UserEntity(name, passw, detail)
                if (!TextUtils.isEmpty(userEntity.password)) {
                    userEntity.statePassword = true
                }
                viewModel2!!.insertPeopel(userEntity)
                SessionIndexFragment.open(this, ProgressDialog.show(this, "Registration", "Please wait", true, false))
                finish()
            } else {
                UI.alert(this, "#numeric value", "Password should be 4 digit number")
            }
        }

        btn_start.setOnClickListener {
             getInfo(vs)
        }
    }

    fun gotoActivity() {
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    fun getInfo(vs: ViewSetup) {
        if (viewModel2?.getAllPeopleInfo() != null &&
                viewModel2!!.getAllPeopleInfo()!!.isEmpty()) {

            buildDialog(vs)

        } else {
            DialogBuilder().title("Error")
                    .message("Resigration failed!....")
                    .positive {
                        if (viewModel2!!.getAllPeopleInfo()!![0].statePassword) {
//                            PasswordLogin().show(supportFragmentManager, "")
//                            finish()
                        } else {
                            SessionIndexFragment.open(this, ProgressDialog.show(this, "Registration", "Please wait", true, false))
                            finish()
                        }
                    }
                    .negative {
                        Mold.makeSnackBar(this, "Successfully done...")
                    }.show(this)
        }
    }

    fun buildDialog(vs: ViewSetup) {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Registration")
        builder.setCancelable(false)
        builder.setView(vs.view)
        builder.create()
        builder.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Data.destroyInstance()
    }
}