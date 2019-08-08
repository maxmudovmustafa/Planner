package com.example.myapplicatio.util

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.myapplicatio.R
import uz.greenwhite.lib.Tuple2
import java.io.ByteArrayOutputStream

class ImagesList(var context: Context,
                 private var mData: ArrayList<Tuple2>,
                 var mClick: ItemClickListener)
    : RecyclerView.Adapter<ImagesList.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.image_list, parent, false))
    }

    fun addItem(value: Tuple2) {
        mData.add(value)
        notifyDataSetChanged()
    }

    fun getItems(): ArrayList<ByteArray>{
        var result = ArrayList<ByteArray>()
        mData.forEach {
//            Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
            var stream = ByteArrayOutputStream()
            (it.first as Bitmap).compress(Bitmap.CompressFormat.JPEG, 100, stream)
            var bitmapdata = stream.toByteArray()
            result.add(bitmapdata)

        }
        return result
    }
    fun removeItem(position: Int) {
        mData.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mData[position].first != null) {
            holder.image.setImageBitmap(mData[position].first as Bitmap)
            mClick.onItemClick(mData[position].second as String)
        }
    }


    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var image: ImageView = itemView.findViewById(R.id.igm_list)

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