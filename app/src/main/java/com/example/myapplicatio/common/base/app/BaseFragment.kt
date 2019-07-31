package com.example.myapplicatio.common.base.app

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseFragment : Fragment() {

    protected var mActivity: Activity? = null
    protected var mView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mActivity = activity
        mView = initContentView(inflater, container)
        if (mView == null)
            throw NullPointerException("Fragment content view is null.")
        bindView()
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    protected abstract fun initContentView(inflater: LayoutInflater, container: ViewGroup?): View?

    override fun onResume() {
        super.onResume()
        bindData()
    }

    protected abstract fun bindView()

    protected open fun initData() {

    }

    protected open fun bindData() {

    }

    protected fun <VT : View> searchViewById(id: Int): VT {
        if (mView == null)
            throw NullPointerException("Fragment content view is null.")
        return mView!!.findViewById<View>(id) as VT
    }

}
