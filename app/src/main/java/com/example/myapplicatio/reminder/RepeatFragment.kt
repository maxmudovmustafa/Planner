package com.example.myapplicatio.reminder

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.myapplicatio.R
import com.example.myapplicatio.util.ToastUtils
import kotlinx.android.synthetic.main.repeat_reminder.*
import uz.greenwhite.lib.Command
import uz.greenwhite.lib.collection.MyArray
import uz.greenwhite.lib.mold.Mold
import uz.greenwhite.lib.mold.MoldContentFragment
import uz.greenwhite.lib.view_setup.DialogBuilder
import uz.greenwhite.lib.view_setup.PopupBuilder
import uz.greenwhite.lib.view_setup.UI

class RepeatFragment : MoldContentFragment() {

    companion object {
        fun openContent(click_Reminder: ReminderContentFragment, fragmentManager: FragmentManager, message: Int) {
            var fragmen = RepeatFragment()
            fragmen.listen = click_Reminder
            fragmen.apply { arguments = Bundle().apply { putInt("repeat", message) } }
            fragmentManager.beginTransaction().replace(R.id.ll_container, fragmen, "ssss").commit()
        }
    }

    private var message: Int = 1
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        arguments?.getInt("repeat")?.let { message = it }
    }

    private var listen: Click_Reminder? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.repeat_reminder, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewGone(iv_1, message)
        ll_donot_repeat.setOnClickListener {
            listen?.onClickValue(1, "Don not repeat")

            var s = childFragmentManager.findFragmentByTag("ssss")
                    childFragmentManager.beginTransaction().remove(s).commit()
            childFragmentManager.popBackStack()
        }
        tv_repeat_every_day.setOnClickListener {
            listen?.onClickValue(2, "Repeat every day")
            childFragmentManager.beginTransaction().remove(this).commit()
        }
        tv_repeat_every_weel.setOnClickListener {
            listen?.onClickValue(3, "Repeat every week")
            childFragmentManager.beginTransaction().remove(this).commit()
        }
        tv_repeat_every_month.setOnClickListener {
            listen?.onClickValue(4, "Reapeat every month")
            childFragmentManager.beginTransaction().remove(this).commit()
        }
        tv_custom.setOnClickListener {
            listen?.onClickValue(5, "Custom")
            childFragmentManager.beginTransaction().remove(this).commit()
        }
    }

    fun viewGone(iv_1: ImageView, message: Int) {
        when(message) {
            1 -> {
                iv_1.visibility = View.VISIBLE
                iv_2.visibility = View.INVISIBLE
                iv_3.visibility = View.INVISIBLE
                iv_4.visibility = View.INVISIBLE
                iv_5.visibility = View.INVISIBLE
            }
            2 -> {
                iv_2.visibility = View.VISIBLE
                iv_1.visibility = View.INVISIBLE
                iv_3.visibility = View.INVISIBLE
                iv_4.visibility = View.INVISIBLE
                iv_5.visibility = View.INVISIBLE
            }
            3 -> {
                iv_3.visibility = View.VISIBLE
                iv_2.visibility = View.INVISIBLE
                iv_1.visibility = View.INVISIBLE
                iv_4.visibility = View.INVISIBLE
                iv_5.visibility = View.INVISIBLE
            }
            4 -> {
                iv_4.visibility = View.VISIBLE
                iv_2.visibility = View.INVISIBLE
                iv_3.visibility = View.INVISIBLE
                iv_1.visibility = View.INVISIBLE
                iv_5.visibility = View.INVISIBLE
            }
            5 -> {
                iv_5.visibility = View.VISIBLE
                iv_2.visibility = View.INVISIBLE
                iv_3.visibility = View.INVISIBLE
                iv_4.visibility = View.INVISIBLE
                iv_1.visibility = View.INVISIBLE
            }
        }
    }
}