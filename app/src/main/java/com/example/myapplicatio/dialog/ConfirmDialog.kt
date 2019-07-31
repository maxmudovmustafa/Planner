package com.example.myapplicatio.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.TextView

import com.example.myapplicatio.R


class ConfirmDialog @JvmOverloads constructor(context: Context,
                                              private val mTitle: String,
                                              private val mOnClickListener: OnClickListener?,
                                              private val mAutoDismiss: Boolean = true) :
        Dialog(context, R.style.DialogFullScreen), View.OnClickListener {

    private var tvTitle: TextView? = null

    constructor(context: Context,
                id: Int,
                onClickListener: OnClickListener) : this(context, context.getString(id), onClickListener) {}

    init {
        setContentView(R.layout.dialog_confirm)
        tvTitle = findViewById<View>(R.id.tvTitle) as TextView
        tvTitle!!.text = mTitle
        findViewById<View>(R.id.tvCancel).setOnClickListener(this)
        findViewById<View>(R.id.tvConfirm).setOnClickListener(this)
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvCancel -> {
                mOnClickListener?.onCancel()
            }
            R.id.tvConfirm -> {
                mOnClickListener?.onConfirm()
            }
        }
        dismiss()
    }

    interface OnClickListener {
        fun onCancel()
        fun onConfirm()
    }

    override fun dismiss() {
        if (mAutoDismiss) {
            super.dismiss()
        }
    }
}
