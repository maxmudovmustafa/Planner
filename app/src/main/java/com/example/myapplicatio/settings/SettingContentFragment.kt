package com.example.myapplicatio.settings

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplicatio.R
import com.example.myapplicatio.util.ToastUtils
import kotlinx.android.synthetic.main.f_setting.*
import uz.greenwhite.lib.mold.Mold
import uz.greenwhite.lib.mold.MoldContentFragment


class SettingContentFragment : MoldContentFragment() {
    companion object {
        fun newInstance(): MoldContentFragment {
            var s = Bundle()
            s.putString("Dashboard", "Dashboard")
            return Mold.parcelableArgumentNewInstance(SettingContentFragment::class.java, s)
        }
    }

    private var doubleBackToExitPressedOnce: Long = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.f_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSnackbarButton.setOnClickListener {
            Snackbar.make(coordinatorLayout,
                    "This is a simple Snackbar", Snackbar.LENGTH_LONG)
                    .setAction("CLOSE") {
                        // Custom action
                    }.show()
        }
    }

    override fun onBackPressed(): Boolean {
        if (doubleBackToExitPressedOnce + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            return false
        } else ToastUtils.showLongToast(context, "Press once again to exit!")
        doubleBackToExitPressedOnce = System.currentTimeMillis()
        return true
    }
}