package com.example.myapplicatio.common.base.app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View


abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView()
        initData()
    }

    override fun onResume() {
        super.onResume()
        bindData()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    protected abstract fun bindView()

    protected open fun initData() {

    }

    protected open fun bindData() {

    }

    protected fun <VT : View> searchViewById(id: Int): VT {
        return findViewById<View>(id) as VT
    }

}
