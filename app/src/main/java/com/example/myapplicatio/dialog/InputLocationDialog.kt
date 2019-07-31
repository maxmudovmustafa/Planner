package com.example.myapplicatio.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.EditText

import com.example.myapplicatio.R

class InputLocationDialog(context: Context,
                          private val mOnLocationBackListener: OnLocationBackListener?)
    : Dialog(context, R.style.DialogFullScreen), View.OnClickListener {

    private var etLocationContent: EditText? = null

    init {
        setContentView(R.layout.dialog_input_location)
        etLocationContent = findViewById(R.id.etLocationContent)
        findViewById<View>(R.id.tvCancel).setOnClickListener(this)
        findViewById<View>(R.id.tvConfirm).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvCancel -> dismiss()
            R.id.tvConfirm -> {
                mOnLocationBackListener?.onLocationBack(etLocationContent!!.text.toString())
                dismiss()
            }
        }
    }

    interface OnLocationBackListener {
        fun onLocationBack(text: String)
    }
}
