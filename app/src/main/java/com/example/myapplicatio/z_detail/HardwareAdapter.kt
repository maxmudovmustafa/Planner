package com.example.myapplicatio.z_detail

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.myapplicatio.R

class HardwareAdapter(var context: Context,
                      private var mData: ArrayList<MyTextSample>,
                      var mClick: ItemClickListener)
    : RecyclerView.Adapter<HardwareAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.simple_item_1, parent, false))
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val values = mData[position]
        holder.title.text = values.name
        holder.info.text = values.detail
        holder.ll_con.setOnClickListener {
            mClick.onItemClick(values)
        }
    }


    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var info: TextView = itemView.findViewById(R.id.tv_info)
        var title: TextView = itemView.findViewById(R.id.tv_title)
        var ll_con: LinearLayout = itemView.findViewById(R.id.ll_con)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

        }
    }

    interface ItemClickListener {
        fun onItemClick(position: MyTextSample)
    }

}