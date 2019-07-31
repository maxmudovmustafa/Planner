package com.example.myapplicatio.adapter

import android.content.Context
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.example.myapplicatio.R
import com.example.myapplicatio.common.bean.EventSet
import com.example.myapplicatio.common.listener.OnTaskFinishedListener
import com.example.myapplicatio.dialog.ConfirmDialog
import com.example.myapplicatio.task.eventset.RemoveEventSetTask
import com.example.myapplicatio.utils.JeekUtils
import com.example.myapplicatio.widget.SlideDeleteView

class EventSetAdapter(private val mContext: Context, private val mEventSets: MutableList<EventSet>)
    : RecyclerView.Adapter<EventSetAdapter.EventSetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventSetViewHolder {
        return EventSetViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_event_set, parent, false))
    }

    override fun getItemCount(): Int {
        return mEventSets.size
    }

    override fun onBindViewHolder(holder: EventSetViewHolder, position: Int) {
        val eventSet = mEventSets[position]
        holder.sdvEventSet.close(false)
        holder.tvEventSetName.text = eventSet.name
        holder.vEventSetColor.setBackgroundResource(JeekUtils.getEventSetColor(eventSet.color))
        holder.ibEventSetDelete.setOnClickListener { showDeleteEventSetDialog(eventSet, position) }
//        holder.sdvEventSet.setOnContentClickListener { gotoEventSetFragment(eventSet) }
    }

    private fun showDeleteEventSetDialog(eventSet: EventSet, position: Int) =
            ConfirmDialog(mContext, R.string.event_set_delete_this_event_set, object : ConfirmDialog.OnClickListener {
                override fun onCancel() {}

                override fun onConfirm() {
                    RemoveEventSetTask(mContext, object : OnTaskFinishedListener<Boolean> {
                        override fun onTaskFinished(data: Boolean) {
                            if (data) {
                                removeItem(position)
                            }
                        }
                    }, eventSet.id).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                }
            }).show()

//    private fun gotoEventSetFragment(eventSet: EventSet) {}

    inner class EventSetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        internal val sdvEventSet: SlideDeleteView = itemView.findViewById(R.id.sdvEventSet)
        internal val vEventSetColor: View = itemView.findViewById(R.id.vEventSetColor)
        internal val tvEventSetName: TextView = itemView.findViewById<View>(R.id.tvEventSetName) as TextView
        internal val ibEventSetDelete: ImageButton = itemView.findViewById<View>(R.id.ibEventSetDelete) as ImageButton
    }

    fun changeAllData(eventSets: List<EventSet>) {
        mEventSets.clear()
        mEventSets.addAll(eventSets)
        notifyDataSetChanged()
    }

    fun insertItem(eventSet: EventSet) {
        mEventSets.add(eventSet)
        notifyItemInserted(mEventSets.size - 1)
    }

    fun removeItem(position: Int) {
        mEventSets.removeAt(position)
        notifyDataSetChanged()
    }
}
