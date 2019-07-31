package com.example.myapplicatio.calendar

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myapplicatio.R
import com.example.myapplicatio.adapter.EventSetAdapter
import com.example.myapplicatio.common.bean.EventSet
import uz.greenwhite.lib.mold.Mold
import uz.greenwhite.lib.mold.MoldContentFragment
import java.util.*

class CalendarFragment : MoldContentFragment() {
    companion object {

    }

    private var floatingActionButton : FloatingActionButton? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.f_calendar, container, false)
    }

    override fun onStart() {
        super.onStart()
        createFloatingButtons()
    }

    override fun onStop() {
        super.onStop()
        if (floatingActionButton == null) {
            floatingActionButton = Mold.makeFloatAction(activity, R.drawable.ic_add)
        }

        val lp = CoordinatorLayout.LayoutParams(floatingActionButton?.layoutParams)
        val padding16 = resources.getDimension(R.dimen.padding_16dp).toInt()

        lp.setMargins(padding16, padding16, padding16, padding16)
        lp.gravity = Gravity.BOTTOM or Gravity.RIGHT
        floatingActionButton?.layoutParams = lp
    }

    fun hasItemDivider(): Boolean {
        return false
    }

    private fun createFloatingButtons() {

        val floatingActionButton = Mold.makeFloatAction(activity, R.drawable.ic_add)
        val lp = CoordinatorLayout.LayoutParams(floatingActionButton.layoutParams)
        val padding16 = resources.getDimension(R.dimen.padding_16dp).toInt()
        val padding80 = resources.getDimension(R.dimen.padding_80dp).toInt()

        lp.setMargins(padding16, padding16, padding16, padding80)
        lp.gravity = Gravity.BOTTOM or Gravity.RIGHT
        floatingActionButton.layoutParams = lp
        floatingActionButton.setOnClickListener(View.OnClickListener {
            Toast.makeText(context, "Floating Action Button", Toast.LENGTH_SHORT).show()

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}