package com.example.myapplicatio.memos

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.ImageView
import com.example.myapplicatio.R
import uz.greenwhite.lib.view_setup.ViewSetup

@SuppressLint("ValidFragment")
class DialogColor (listen: ColorPick) : DialogFragment() {
    private var listener: ColorPick? = null
    init {
    this.listener = listen
    }

    fun click(view: ImageView){
        view.setOnClickListener {
            listener!!.getColor(view.drawable)
            dismiss()
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var dialog = AlertDialog.Builder(context!!)
        var vs = ViewSetup(context, R.layout.color_picer)

        if (listener != null) {
            click(vs.imageView(R.id.color_1))
            click(vs.imageView(R.id.color_2))
            click(vs.imageView(R.id.color_3))
            click(vs.imageView(R.id.color_4))
            click(vs.imageView(R.id.color_5))
            click(vs.imageView(R.id.color_6))
            click(vs.imageView(R.id.color_7))
            click(vs.imageView(R.id.color_8))
            click(vs.imageView(R.id.color_9))
            click(vs.imageView(R.id.color_10))
        }

        vs.button(R.id.btn_cancel).setOnClickListener {
            dismiss()
        }

        dialog.setView(vs.view)
        return dialog.create()
    }

    interface ColorPick {
        fun getColor(color: Drawable)

    }
}