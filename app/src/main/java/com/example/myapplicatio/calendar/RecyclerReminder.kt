package com.example.myapplicatio.calendar

import android.content.Context
import android.graphics.Paint
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ToggleButton
import com.example.myapplicatio.R

class RecyclerReminder(var context: Context,
                       private var mData: ArrayList<String>,
                       var mClick: ItemClickListener)
    : RecyclerView.Adapter<RecyclerReminder.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.sample_list_2, parent, false))
    }

    fun addItem(value: String) {
        mData.add(value)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.info.text = mData[position]
        holder.checked.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                holder.info.paintFlags = holder.info.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                holder.checked.background = context.getDrawable(R.drawable.ic_selected_box)
            } else {
                holder.checked.background = context.getDrawable(R.drawable.ic_check_box_empty)
                holder.info.paintFlags = holder.info.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        holder.checkLine.setOnClickListener {
            if (!holder.info.paint.isStrikeThruText) {
                holder.info.paintFlags = holder.info.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                holder.checked.background = context.getDrawable(R.drawable.ic_selected_box)
            } else {
                holder.checked.background = context.getDrawable(R.drawable.ic_check_box_empty)
                holder.info.paintFlags = holder.info.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
            mClick.onItemClick(mData[position])
        }
    }


    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var info: TextView = itemView.findViewById(R.id.tv_name_reminder)
        var checked: ToggleButton = itemView.findViewById(R.id.tg_check)
        var checkLine: LinearLayout = itemView.findViewById(R.id.ll_check)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

        }
    }

    interface ItemClickListener {
        fun onItemClick(position: String)
    }
}