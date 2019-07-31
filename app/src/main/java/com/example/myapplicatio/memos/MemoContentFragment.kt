package com.example.myapplicatio.memos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplicatio.R
import com.example.myapplicatio.util.ToastUtils
import uz.greenwhite.lib.mold.Mold
import uz.greenwhite.lib.mold.MoldContentFragment

class MemoContentFragment : MoldContentFragment() {
    companion object {
        fun newInstance(): MoldContentFragment {
            var s = Bundle()
            s.putString("Dashboard", "Dashboard")
            return Mold.parcelableArgumentNewInstance(MemoContentFragment::class.java, s)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.f_note, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private var doubleBackToExitPressedOnce : Long = 0
    override fun onBackPressed(): Boolean {
        if (doubleBackToExitPressedOnce + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            return false
        }
        else ToastUtils.showLongToast(context, "Press once again to exit!")
        doubleBackToExitPressedOnce = System.currentTimeMillis()
        return true
    }
}