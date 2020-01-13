package com.example.half_bloodprince.test

import android.app.ActivityOptions
import android.app.ProgressDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.transition.Slide
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.ToggleButton
import com.example.myapplicatio.R
import com.example.myapplicatio.dashboard.SessionIndexFragment
import com.example.myapplicatio.db.Data
import com.example.myapplicatio.db.user.UserEntity
import com.example.myapplicatio.db.user.UserModelView
import com.example.myapplicatio.util.ToastUtils
import kotlinx.android.synthetic.main.splash_screen.*
import uz.greenwhite.lib.view_setup.DialogBuilder
import uz.greenwhite.lib.view_setup.ViewSetup

class FireIntro : Fragment() {

    private var mDelay: Handler? = null
    private var vs: ViewSetup? = null

    fun gotoActivity() {
        if (Build.VERSION.SDK_INT > 20) {
            var option = ActivityOptions.makeSceneTransitionAnimation(activity)
            var intent = Intent(context, MainActivity::class.java)
            startActivity(intent, option.toBundle())
            activity!!.finish()
        }
    }

    fun getInfo(vs: ViewSetup) {
        buildDialog(vs)
    }

    fun buildDialog(vs: ViewSetup) {
        var builder = AlertDialog.Builder(context!!)
        builder.setTitle("Registration")
        builder.setCancelable(false)
        builder.setView(vs.view)
        builder.create()
        builder.show()
    }

    override fun onDestroy() {
        Data.destroyInstance()
        if (mDelay != null) {
            mDelay!!.removeCallbacks(mRunnable)
        }
        super.onDestroy()
    }

    private val mRunnable = Runnable {
        if (!activity!!.isFinishing) {
            getInfo(vs!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAnimation()
        vs = ViewSetup(layoutInflater.inflate(R.layout.dialog_registration, null, false))

        var anim = AnimationUtils.loadAnimation(context!!, R.anim.my_transmission)
        my_splash.startAnimation(anim)
        tv_splash.startAnimation(anim)

        mDelay = Handler()
        mDelay!!.postDelayed(mRunnable, 2000)
        val editText = vs!!.editText(R.id.ed_user_passw)

        vs!!.compoundButton<ToggleButton>(R.id.tb_see_password).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            else
                editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }
        vs!!.button(R.id.btn_cancel).setOnClickListener { gotoActivity() }

        vs!!.button(R.id.btn_registr).setOnClickListener {
            val name = vs!!.editText(R.id.ed_user_name).text.toString()
            val passw = editText.text.toString()
            val detail = vs!!.editText(R.id.ed_user_detail).text.toString()
            if (passw.isEmpty() || passw.length == 4) {
                val userEntity = UserEntity(name, passw, detail)
                if (!TextUtils.isEmpty(userEntity.password)) {
                    userEntity.statePassword = true
                }

                if (TextUtils.isEmpty(name)) {
                    vs!!.editText(R.id.ed_user_name).error = "This filed must be empty"
                } else {
                    ToastUtils.showShortToast(context, "Succecffully Done!!")
                }
            }
        }
    }

    fun setAnimation() {
        if (Build.VERSION.SDK_INT > 20) {
            var slide = Slide()
            slide.slideEdge = Gravity.LEFT
            slide.duration = 5000
            slide.interpolator = DecelerateInterpolator()
            activity!!.window.exitTransition = slide
            activity!!.window.enterTransition = slide
        }
    }

}