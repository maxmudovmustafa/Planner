package com.example.myapplicatio.aralash

import android.app.ProgressDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.myapplicatio.R
import com.example.myapplicatio.dashboard.SessionIndexFragment
import com.example.myapplicatio.db.user.UserModelView
import com.example.myapplicatio.util.ToastUtils
import kotlinx.android.synthetic.main.password_layout.*
import uz.greenwhite.lib.view_setup.ViewSetup

class PasswordLogin : Fragment(), View.OnClickListener {

    private var password: String = ""
    private var userPassword: String = ""
    var vs: ViewSetup? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        vs = ViewSetup(inflater.inflate(R.layout.password_layout, container, false))
        return vs!!.view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ll_btn_0.setOnClickListener(this)
        ll_btn_1.setOnClickListener(this)
        ll_btn_2.setOnClickListener(this)
        ll_btn_3.setOnClickListener(this)
        ll_btn_4.setOnClickListener(this)
        ll_btn_5.setOnClickListener(this)
        ll_btn_6.setOnClickListener(this)
        ll_btn_7.setOnClickListener(this)
        ll_btn_8.setOnClickListener(this)
        ll_btn_9.setOnClickListener(this)
        ll_btn_remove.setOnClickListener(this)

        tv_cancel.setOnClickListener {
            activity?.finish()
        }

        /**
         * Blur Image
         */
//        val bimtap = BitmapFactory.decodeResource(resources, R.drawable.ic_calendar_r)
//        val createScaledBitmap = Bitmap.createScaledBitmap(bimtap, 10, 10, true)
//        val create = RoundedBitmapDrawableFactory.create(resources, createScaledBitmap)
//        ll_blur.setImageBitmap(createScaledBitmap)
        /**
         *
         */

        var factory = UserViewFactory(App.getApplicationContext(context!!))
        var viewModel = ViewModelProviders.of(activity!!, factory).get(UserModelView::class.java).getAllPeopleInfo()
        if (!viewModel.isNullOrEmpty()) {
            userPassword = viewModel[0].password
        }
    }

    override fun onClick(v: View?) {
        if (v!!.id == R.id.ll_btn_remove) {
            if (TextUtils.isEmpty(password)) {
                enterCode(false)
            }
        } else {
            enterPassword(v)
        }
    }

    private fun enterPassword(tag: View) {
        password += tag.tag.toString()
        enterCode(true)
        if (password.length == 4) {
            if (TextUtils.equals(password, userPassword)) {
                SessionIndexFragment.open(activity!!, ProgressDialog.show(context, "Registration", "Please wait", true, false))
                activity!!.finish()
            } else {
                val shake = AnimationUtils.loadAnimation(context!!, R.anim.shake)
                rl_keyboard.startAnimation(shake)
                resetPassword()
            }
        }
    }

    private fun resetPassword() {
        password = ""
        ToastUtils.showShortToast(context!!, "Wrong Password!!!")
        view().forEach {
            it.setBackgroundResource(R.drawable.empty_dot)
        }
    }

    private fun enterCode(state: Boolean) {
        if (password.length < 4)
            resultCode(view()[password.length - 1], state)
    }

    fun view(): ArrayList<View> = arrayListOf<View>(v_code_1, v_code_2, v_code_3, v_code_4)

    fun resultCode(view: View, state: Boolean) {
        if (state) {
            view.setBackgroundResource(R.drawable.filled_dot)
        } else {
            password = if (password.length == 1) "" else password.substring(0, password.length - 1)
            view.setBackgroundResource(R.drawable.empty_dot)
        }
    }

    override fun onStop() {
        super.onStop()
        activity?.finish()
    }

    /* inner class FingerprintHelper internal constructor(private val mContext: Context) : FingerprintManagerCompat.AuthenticationCallback() {
         private var mCancellationSignal: CancellationSignal? = null

         internal fun startAuth(cryptoObject: FingerprintManagerCompat.CryptoObject) {
             mCancellationSignal = CancellationSignal()
             val manager = FingerprintManagerCompat.from(mContext)
             manager.authenticate(cryptoObject, 0, mCancellationSignal, this, null)
         }

         internal fun cancel() {
             if (mCancellationSignal != null) {
                 mCancellationSignal!!.cancel()
             }
         }

         override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {}

         override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence?) {}

         override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
             val cipher = result!!.cryptoObject.cipher

         }

         override fun onAuthenticationFailed() {

         }

     }*/
}