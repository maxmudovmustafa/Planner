package com.example.myapplicatio.fragment

import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.AsyncTask
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.myapplicatio.R
import com.example.myapplicatio.adapter.ScheduleAdapter
import com.example.myapplicatio.aralash.App
import com.example.myapplicatio.aralash.ReminderViewFactory
import com.example.myapplicatio.aralash.SwipeHelper
import com.example.myapplicatio.aralash.SwipeToDeleteCallback
import com.example.myapplicatio.calendar.OnCalendarClickListener
import com.example.myapplicatio.calendar.RecyclerReminder
import com.example.myapplicatio.calendar.schedule.ScheduleLayout
import com.example.myapplicatio.calendar.schedule.ScheduleRecyclerView
import com.example.myapplicatio.common.base.app.BaseFragment
import com.example.myapplicatio.common.bean.Schedule
import com.example.myapplicatio.common.listener.OnTaskFinishedListener
import com.example.myapplicatio.common.util.DeviceUtils
import com.example.myapplicatio.db.ReminderEntity
import com.example.myapplicatio.db.ReminderModelView
import com.example.myapplicatio.dialog.SelectDateDialog
import com.example.myapplicatio.task.schedule.AddScheduleTask
import com.example.myapplicatio.task.schedule.LoadScheduleTask
import com.example.myapplicatio.util.CalendarUtil
import com.example.myapplicatio.util.ToastUtils
import kotlinx.android.synthetic.main.layout_schedule.*
import java.util.*


class ScheduleFragment : BaseFragment(),
        OnCalendarClickListener,
        View.OnClickListener,
        OnTaskFinishedListener<List<Schedule>>,
        SelectDateDialog.OnSelectDateListener {

    private var slSchedule: ScheduleLayout? = null
    private var rvScheduleList: ScheduleRecyclerView? = null
    private var etInputContent: EditText? = null
    private var rlNoTask: RelativeLayout? = null
    private var mScheduleAdapter: ScheduleAdapter? = null
    private var mCurrentSelectYear: Int = 0
    private var mCurrentSelectMonth: Int = 0
    private var mCurrentSelectDay: Int = 0
    private var mTime: Long = 0

    val currentCalendarPosition: Int
        get() = slSchedule!!.monthCalendar.currentItem

    override fun initContentView(inflater: LayoutInflater, container: ViewGroup?): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }


    override fun bindView(){

        val factory = ReminderViewFactory(App.getApplicationContext(context!!))
        val modelView = ViewModelProviders.of(this, factory).get(ReminderModelView::class.java)
        var rv_list = searchViewById<RecyclerView>(R.id.rv_list)
        val allReminder = modelView.getAllReminder()
        rv_list.layoutManager = LinearLayoutManager(context)
        val adapter = RecyclerReminder(context!!, ArrayList(allReminder!!), object : RecyclerReminder.ItemClickListener {
            override fun onItemClick(position: ReminderEntity) {
                ToastUtils.showShortToast(context, position.title)
            }
        })

        var swipe = object : SwipeToDeleteCallback(context!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeItem(viewHolder.adapterPosition)
            }
        }

        var itemTouchhelper = ItemTouchHelper(swipe)
        itemTouchhelper.attachToRecyclerView(rv_list)
        rv_list.adapter = adapter

        slSchedule = searchViewById<ScheduleLayout>(R.id.slSchedule)
        rvScheduleList = searchViewById<ScheduleRecyclerView>(R.id.rvScheduleList)
        etInputContent = searchViewById<EditText>(R.id.etInputContent)
        rlNoTask = searchViewById<RelativeLayout>(R.id.rlNoTask)
        slSchedule!!.setOnCalendarClickListener(this)
        searchViewById<View>(R.id.ibMainClock).setOnClickListener(this)
        searchViewById<View>(R.id.ibMainOk).setOnClickListener(this)
        searchViewById<View>(R.id.ivMainMenu).setOnClickListener(this)
        searchViewById<View>(R.id.llTitleDate).setOnClickListener(this)
        initScheduleList()
        initBottomInputBar()
    }

    override fun initData() {
        super.initData()
        initDate()
    }

    override fun bindData() {
        super.bindData()
        resetScheduleList()
    }

    fun resetScheduleList() {
        LoadScheduleTask(mActivity, this, mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    private fun initDate() {
        val calendar = Calendar.getInstance()
        setCurrentSelectDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
    }

    override fun onClickDate(year: Int, month: Int, day: Int) {
        setCurrentSelectDate(year, month, day)
        resetScheduleList()
        getListReminder()
    }

    fun getListReminder() {
        ToastUtils.showShortToast(context, "ss")
        val t = searchViewById<View>(R.id.tvTitleMonth) as TextView
        val day = searchViewById<View>(R.id.tvTitleDay) as TextView
        t.text = CalendarUtil.MONTH(mCurrentSelectMonth)
        day.text = " $mCurrentSelectDay"
    }

    override fun onPageChange(year: Int, month: Int, day: Int) {

    }

    private fun initScheduleList() {
        val manager = LinearLayoutManager(mActivity)
        manager.orientation = LinearLayoutManager.VERTICAL
        rvScheduleList!!.layoutManager = manager
        val itemAnimator = DefaultItemAnimator()
        itemAnimator.supportsChangeAnimations = false
        rvScheduleList!!.itemAnimator = itemAnimator

        mScheduleAdapter = ScheduleAdapter(mActivity, this)
        rvScheduleList!!.adapter = mScheduleAdapter

        //        var items = ArrayList<MyTextSample>()
        //        items.add(MyTextSample("Mine", "Test1"))
        //        items.add(MyTextSample("Mine", "Test2"))
        //        items.add(MyTextSample("Mine", "Test3"))
        //        items.add(MyTextSample("Mine", "Test4"))
        //        items.add(MyTextSample("Mine", "Test5"))
        //        items.add(MyTextSample("Mine", "Test6"))
        //
        //        var adapter = MyListAdapter(context!!, items, object : MyListAdapter.ItemClickListener {
        //            override fun onItemClick(position: MyTextSample) {
        //                Toast.makeText(context, position.detail, Toast.LENGTH_SHORT).show()
        //            }
        //        })
//                list.layoutManager = LinearLayoutManager(context)
//                list.adapter = adapter
    }

    private fun initBottomInputBar() {
        etInputContent!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                etInputContent!!.gravity = if (s.length == 0) Gravity.CENTER else Gravity.CENTER_VERTICAL
            }
        })
        etInputContent!!.setOnKeyListener { v, keyCode, event -> false }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ibMainClock -> showSelectDateDialog()
            R.id.ibMainOk -> addSchedule()

            R.id.ivMainMenu -> {
                run {
                    SelectDateDialog(activity, SelectDateDialog.OnSelectDateListener { year, month, day, time, position -> ToastUtils.showShortToast(context, "year:$year.$month.$day") }, mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay, Calendar.getInstance().get(Calendar.DAY_OF_WEEK)).show()
                }
                run { resetMainTitleDate(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay) }
            }
            R.id.llTitleDate -> {
                resetMainTitleDate(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay)
            }
        }
    }

    private fun resetMainTitleDate(mCurrentSelectYear: Int, mCurrentSelectMonth: Int, mCurrentSelectDay: Int) {
        val day = searchViewById<View>(R.id.tvTitleDay) as TextView
        val month = searchViewById<View>(R.id.tvTitleMonth) as TextView
        day.text = CalendarUtil.DAY()
        month.text = CalendarUtil.MONTH()
    }

    private fun showSelectDateDialog() {
        SelectDateDialog(mActivity, this, mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay, slSchedule!!.monthCalendar.currentItem).show()
    }

    private fun closeSoftInput() {
        etInputContent!!.clearFocus()
        DeviceUtils.closeSoftInput(mActivity!!, etInputContent!!)
    }

    private fun addSchedule() {
        val content = etInputContent!!.text.toString()
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showShortToast(mActivity, R.string.schedule_input_content_is_no_null)
        } else {
            closeSoftInput()
            val schedule = Schedule()
            schedule.title = content
            schedule.state = 0
            schedule.time = mTime
            schedule.year = mCurrentSelectYear
            schedule.month = mCurrentSelectMonth
            schedule.day = mCurrentSelectDay
            AddScheduleTask(mActivity, object : OnTaskFinishedListener<Schedule> {
                override fun onTaskFinished(data: Schedule) {
                    if (data != null) {
                        mScheduleAdapter!!.insertItem(data)
                        etInputContent!!.text.clear()
                        rlNoTask!!.visibility = View.GONE
                        mTime = 0
                        updateTaskHintUi(mScheduleAdapter!!.itemCount - 2)
                    }
                }
            }, schedule).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }

    private fun setCurrentSelectDate(year: Int, month: Int, day: Int) {
        mCurrentSelectYear = year
        mCurrentSelectMonth = month
        mCurrentSelectDay = day
    }

    override fun onTaskFinished(data: List<Schedule>) {
        mScheduleAdapter!!.changeAllData(data)
        rlNoTask!!.visibility = if (data.isEmpty()) View.VISIBLE else View.GONE
        updateTaskHintUi(data.size)
    }

    private fun updateTaskHintUi(size: Int) {
        if (size == 0) {
            slSchedule!!.removeTaskHint(mCurrentSelectDay)
        } else {
            slSchedule!!.addTaskHint(mCurrentSelectDay)
        }
    }

    override fun onSelectDate(year: Int, month: Int, day: Int, time: Long, position: Int) {
        slSchedule!!.monthCalendar.currentItem = position
        slSchedule!!.postDelayed({ slSchedule!!.monthCalendar.currentMonthView.clickThisMonth(year, month, day) }, 100)
        mTime = time
    }

    companion object {

        val instance: ScheduleFragment
            get() = ScheduleFragment()
    }}

  /*  fun swiped(){
        var swipeHelper = SwipeHelper(context, rv_list) {
      @Override
    fun instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<SwipeHelper.UnderlayButton> underlayButtons) {
        underlayButtons.add(SwipeHelper.UnderlayButton(
                "Delete",
                0,
                Color.parseColor("#FF3C30"),
                SwipeHelper.UnderlayButtonClickListener() {
                    override fun onClick(int pos) {
                        // TODO: onDelete
                    }
                }
        ))

        underlayButtons.add(object :SwipeHelper.UnderlayButton(
                "Transfer",
                0,
                Color.parseColor("#FF9502"),
                object :SwipeHelper.UnderlayButtonClickListener() {
                    override fun onClick(pos: Int) {
                        // TODO: OnTransfer
                    }
                }
        ))
        underlayButtons.add(object :SwipeHelper.UnderlayButton(
                "Unshare",
                0,
                Color.parseColor("#C7C7CB"),
                object :SwipeHelper.UnderlayButtonClickListener() {
                    override fun onClick(pos: Int) {
                        // TODO: OnUnshare
                    }
                }
        ))
    }
}
    }
}*/
