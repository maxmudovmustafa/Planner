package com.example.myapplicatio.aralash

import android.os.Bundle
import com.example.myapplicatio.R

class ListActivity : BaseActivity() {

    companion object {
        var TAG: String = "List_Tag"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_info)

//        var fr = supportFragmentManager
//        var list  = fr.findFragmentByTag(TAG) as ListFragment


//        addFragmentToActivity(fr, list, R.id.root_activity_list, TAG)
    }
}