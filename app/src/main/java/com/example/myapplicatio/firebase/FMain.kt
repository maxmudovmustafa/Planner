package com.example.myapplicatio.firebase

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import com.example.myapplicatio.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.m_main.*

class FMain : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.m_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(m_toolbar as Toolbar)
        (activity as AppCompatActivity).title = "FireChat"
        setHasOptionsMenu(true)


        val fragmentAdapter = MyPagerAdapter(childFragmentManager)
        viewpager_main.adapter = fragmentAdapter

        tb_layout.setupWithViewPager(viewpager_main)
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.drawer, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId === R.id.nav_first_fragment) {
            FirebaseAuth.getInstance().signOut()
            activity!!.supportFragmentManager.popBackStack()
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}