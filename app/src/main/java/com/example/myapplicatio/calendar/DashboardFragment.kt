package com.example.myapplicatio.calendar

import android.app.FragmentTransaction
import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myapplicatio.*
import com.example.myapplicatio.common.base.app.BaseFragment
import com.example.myapplicatio.db.memo.MemoEntity
import com.example.myapplicatio.db.reminder.ReminderEntity
import com.example.myapplicatio.firebase.FireFragment
import com.example.myapplicatio.firebase.SecondFragment
import com.example.myapplicatio.fragment.ScheduleFragment
import com.example.myapplicatio.memos.MemoContentFragment
import com.example.myapplicatio.util.ToastUtils
import com.example.myapplicatio.voice_record.RecordActiv
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.main_container.*
import uz.greenwhite.lib.mold.Mold
import uz.greenwhite.lib.mold.MoldContentFragment
import uz.greenwhite.lib.view_setup.UI
import uz.greenwhite.lib.view_setup.ViewSetup
import java.util.*
import kotlin.collections.ArrayList

class DashboardFragment : MoldContentFragment(), View.OnClickListener {

    companion object {
        fun newInstance(): MoldContentFragment {
            var s = Bundle()
            s.putString("D", "d")
            return Mold.parcelableArgumentNewInstance(DashboardFragment::class.java, s)
        }
    }

    private var adapter: TabAdapter? = null
    private val tabIcons = intArrayOf(R.drawable.ic_calendar, R.drawable.ic_microphone, R.drawable.ic_call)
    private var doubleBackToExitPressedOnce: Long = 0
    private var mMonthText: Array<String>? = null
    private var mCurrentSelectYear: Int? = null
    private var mCurrentSelectMonth: Int? = null
    private var mCurrentSelectDay: Int? = null
    private var fabExpanded: Boolean = true
    private var auth: FirebaseAuth? = null
    private var dialog: ProgressDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.f_calendar, container, false)
    }

    private fun addFragment(fragment: Fragment) {
        childFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
                .replace(R.id.content, fragment, fragment.javaClass.simpleName)
                .commit()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_first_fragment -> {
                val fragment = FireFragment()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_second_fragment -> {
                val fragment = SecondFragment()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        addReminder()
        gotoSchedule()
        closeFabMenu()
        hasItemDivider()
        fab1.setOnClickListener(this)
        fab2.setOnClickListener(this)
        fab3.setOnClickListener(this)
        fabSetting.setOnClickListener(this)
        var vs = ViewSetup(context, R.layout.gwslib_bottom_sheet_dialog_row)
        vs.imageView(R.id.iv_icon).setImageDrawable(resources.getDrawable(R.drawable.ic_note))
        vs.textView(R.id.tv_text).text = "my dialog"
        UI.bottomSheet().contentView(vs.view)

        adapter = TabAdapter(childFragmentManager, context)
        adapter!!.addFragment(Tab1Fragment(), "Tab 1", tabIcons[0])
        adapter!!.addFragment(Tab2Fragment(), "Tab 2", tabIcons[1])
        adapter!!.addFragment(Tab3Fragment(), "Tab 3", tabIcons[2])
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        highLightCurrentTab(0)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                highLightCurrentTab(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
//        val fragmentAdapter = MyPagerAdapter(childFragmentManager)
//        viewpager_main.adapter = fragmentAdapter
//
//        tb_layout.setupWithViewPager(viewpager_main)
    }

    private fun highLightCurrentTab(position: Int) {
        for (i in 0 until tabLayout!!.tabCount) {
            val tab = tabLayout!!.getTabAt(i)!!
            tab.customView = null
            tab.customView = adapter!!.getTabView(i)
        }
        val tab = tabLayout!!.getTabAt(position)!!
        tab.customView = null
        tab.customView = adapter!!.getSelectedTabView(position)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.fab1 -> {
                Mold.replaceContent(activity, MemoContentFragment.newInstance())
                closeFabMenu()
            }
            R.id.fab2 -> {
                dialog = ProgressDialog.show(context, "Registrate", "Wait", true, true)
                dialog!!.create()
                auth = FirebaseAuth.getInstance()
                auth!!.createUserWithEmailAndPassword("warlocked@iclod.com", "123456789").addOnCompleteListener(activity!!,
                        OnCompleteListener<AuthResult> {
                            if (it.isSuccessful) {
                                ToastUtils.showShortToast(context, "Registreted")
                            } else {
                                ToastUtils.showShortToast(context, "NOt worked")
                            }
                            dialog!!.dismiss()
                        })
            }
            R.id.fab3 -> {
                Mold.openContent(RecordActiv::class.java)  //            Mold.openContent(RecordFile::class.java)
                closeFabMenu()
                Toast.makeText(context, "Floating Action Button", Toast.LENGTH_SHORT).show()
            }
            R.id.fabSetting -> {
                if (fabExpanded) {
                    llBackground.setBackgroundColor(0x0000FF00)
                    closeFabMenu()
                } else {
                    llBackground.setBackgroundColor(resources.getColor(R.color.white))
                    openFabMenu()
                }
            }
        }
    }

    fun initUI() {
        mMonthText = resources.getStringArray(R.array.calendar_month)

        var cl = Calendar.getInstance()
        mCurrentSelectYear = cl.get(Calendar.YEAR)
        mCurrentSelectMonth = cl.get(Calendar.MONTH)
        mCurrentSelectDay = cl.get(Calendar.DAY_OF_MONTH)
    }

    fun hasItemDivider(): Boolean {
        return false
    }

    fun setCurrentSelectDate(year: Int, month: Int, day: Int) {
        mCurrentSelectDay = day
        mCurrentSelectMonth = month
        mCurrentSelectYear = year
    }

    fun gotoSchedule() {
        var tr = childFragmentManager.beginTransaction()
        tr.setTransition(FragmentTransaction.TRANSIT_NONE)
        var mScheduleFragment: BaseFragment? = null
        if (mScheduleFragment == null) {
            mScheduleFragment = ScheduleFragment.instance
            tr.add(R.id.flMainContainer, mScheduleFragment)
        }

        var eventSetFragment: BaseFragment? = null
        if (eventSetFragment != null)
            tr.hide(eventSetFragment)
        tr.show(mScheduleFragment)
        tr.commit()
    }

    fun addReminder() {
        var rAdapter = RecyclerReminder(context!!, ArrayList<ReminderEntity>(), object : RecyclerReminder.ItemClickListener {
            override fun onItemClick(position: ReminderEntity) {
                Toast.makeText(context, position.title, Toast.LENGTH_SHORT).show()
            }
        })

        var nAdapter = RecyclerNote(context!!, ArrayList<MemoEntity>(), object : RecyclerNote.ItemClickListener {
            override fun onItemClick(position: MemoEntity) {
                Toast.makeText(context, position.title, Toast.LENGTH_SHORT).show()
            }
        })

        ed_reminder.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->

            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                if (!TextUtils.isEmpty(ed_reminder.text)) {
//                    adapter.addItem(ed_reminder.text.toString())
                    ed_reminder.setText("")
                    return@OnKeyListener true
                }
            }
            return@OnKeyListener false
        })

        iv_reminder.setOnClickListener {
            if (!TextUtils.isEmpty(ed_reminder.text)) {
//                adapter.addItem(ed_reminder.text.toString())
                ed_reminder.setText("")
            }
        }

        rc_reminder.layoutManager = LinearLayoutManager(context)
        rc_note.layoutManager = LinearLayoutManager(context)
        rc_reminder.adapter = rAdapter
        rc_note.adapter = nAdapter
    }

    fun closeFabMenu() {
        stateFab(false)
        fabSetting.setImageResource(R.drawable.ic_add)
    }

    fun openFabMenu() {
        stateFab(true)
        fabSetting.setImageResource(R.drawable.ic_multiply)
    }

    fun stateFab(state: Boolean) {
        var v = if (state) View.VISIBLE else View.INVISIBLE
        fab1.visibility = v
        fab2.visibility = v
        fab3.visibility = v
        fabExpanded = state
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