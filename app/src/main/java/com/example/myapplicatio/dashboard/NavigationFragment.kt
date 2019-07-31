package com.example.myapplicatio.dashboard

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.MenuItem
import com.example.myapplicatio.R
import com.example.myapplicatio.reminder.ReminderContentFragment
import com.example.myapplicatio.reminder.RepeatFragment
import kotlinx.android.synthetic.main.navigate.*
import uz.greenwhite.lib.mold.MoldActivity

class NavigationFragment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigate)
        setSupportActionBar(toolbar)

        nvView.inflateHeaderView(R.layout.f_note)
        setUpDrawerContent(nvView)
//        var sutupToogle = gettoogle()
//        drawerLayout.addDrawerListener(sutupToogle)
    }

//    fun gettoogle(): ActionBarDrawerToggle {
//        return ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
//    }

//    override fun onPostCreate(savedInstanceState: Bundle?) {
//        super.onPostCreate(savedInstanceState)
//        drawerToggle.syncState()
//    }
//
//    override fun onConfigurationChanged(newConfig: Configuration?) {
//        super.onConfigurationChanged(newConfig)
//        drawerToggle.onConfigurationChanged(newConfig)
//    }

    fun setUpDrawerContent(nvView: NavigationView) {
        nvView.setNavigationItemSelectedListener {
            selectedItem(it)
            return@setNavigationItemSelectedListener true
        }
    }

    fun selectedItem(menuItem: MenuItem) {
        var fragment: Fragment?
        when (menuItem.itemId) {
            R.id.nav_first_fragment -> {


                fragment = ReminderContentFragment()
                supportFragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit()
                menuItem.setChecked(true)
                drawer_layout.closeDrawers()
            }
            R.id.nav_second_fragment -> fragment = RepeatFragment()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.home -> {
                openDrawer(Gravity.START)
                return true
            }
        }
//        if (drawerToggle.onOptionsItemSelected(item)) {
//            return true
//        }
        return super.onOptionsItemSelected(item)
    }


    fun openDrawer(gravity: Int) {
        when (gravity) {
        }

        this.drawer_layout.openDrawer(gravity)
    }
}