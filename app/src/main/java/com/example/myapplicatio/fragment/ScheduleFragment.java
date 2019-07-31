package com.example.myapplicatio.fragment;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplicatio.R;
import com.example.myapplicatio.adapter.ScheduleAdapter;
import com.example.myapplicatio.calendar.OnCalendarClickListener;
import com.example.myapplicatio.calendar.schedule.ScheduleLayout;
import com.example.myapplicatio.calendar.schedule.ScheduleRecyclerView;
import com.example.myapplicatio.common.base.app.BaseFragment;
import com.example.myapplicatio.common.bean.Schedule;
import com.example.myapplicatio.common.listener.OnTaskFinishedListener;
import com.example.myapplicatio.common.util.DeviceUtils;
import com.example.myapplicatio.dialog.SelectDateDialog;
import com.example.myapplicatio.task.schedule.AddScheduleTask;
import com.example.myapplicatio.task.schedule.LoadScheduleTask;
import com.example.myapplicatio.util.ToastUtils;

import java.util.Calendar;
import java.util.List;


public class ScheduleFragment extends BaseFragment implements OnCalendarClickListener, View.OnClickListener,
        OnTaskFinishedListener<List<Schedule>>, SelectDateDialog.OnSelectDateListener {

    private ScheduleLayout slSchedule;
    private ScheduleRecyclerView rvScheduleList;
    private EditText etInputContent;
    private RelativeLayout rLNoTask;

    private ScheduleAdapter mScheduleAdapter;
    private int mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay;
    private long mTime;

    public static ScheduleFragment getInstance() {
        return new ScheduleFragment();
    }

    @Nullable
    @Override
    protected View initContentView(LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    protected void bindView() {
        slSchedule = searchViewById(R.id.slSchedule);
        etInputContent = searchViewById(R.id.etInputContent);
        rLNoTask = searchViewById(R.id.rlNoTask);
        slSchedule.setOnCalendarClickListener(this);
        searchViewById(R.id.ibMainClock).setOnClickListener(this);
        searchViewById(R.id.ibMainOk).setOnClickListener(this);
        searchViewById(R.id.ivMainMenu).setOnClickListener(this);
        searchViewById(R.id.llTitleDate).setOnClickListener(this);
        initScheduleList();
        initBottomInputBar();
    }

    @Override
    protected void initData() {
        super.initData();
        initDate();
    }

    @Override
    protected void bindData() {
        super.bindData();
        resetScheduleList();
    }

    public void resetScheduleList() {
        new LoadScheduleTask(getMActivity(), this, mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initDate() {
        Calendar calendar = Calendar.getInstance();
        setCurrentSelectDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClickDate(int year, int month, int day) {
        setCurrentSelectDate(year, month, day);
        resetScheduleList();
        getListReminder();
    }

    public void getListReminder() {
        ToastUtils.showShortToast(getContext(), "ss");
        TextView t = (TextView) searchViewById(R.id.tvTitleMonth);
        TextView day = (TextView) searchViewById(R.id.tvTitleDay);
        t.setText(" " + getMonthName(mCurrentSelectMonth));
        day.setText(" " + mCurrentSelectDay);
    }

    @Override
    public void onPageChange(int year, int month, int day) {

    }

    private void initScheduleList() {
        rvScheduleList = slSchedule.getSchedulerRecyclerView();
        LinearLayoutManager manager = new LinearLayoutManager(getMActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvScheduleList.setLayoutManager(manager);
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);
        rvScheduleList.setItemAnimator(itemAnimator);

        mScheduleAdapter = new ScheduleAdapter(getMActivity(), this);
        rvScheduleList.setAdapter(mScheduleAdapter);

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
//        list.layoutManager = LinearLayoutManager(context)
//        list.adapter = adapter
    }

    private void initBottomInputBar() {
        etInputContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etInputContent.setGravity(s.length() == 0 ? Gravity.CENTER : Gravity.CENTER_VERTICAL);
            }
        });
        etInputContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibMainClock:
                showSelectDateDialog();
                break;
            case R.id.ibMainOk:
                addSchedule();
                break;

            case R.id.ivMainMenu: {
                new SelectDateDialog(getActivity(), new SelectDateDialog.OnSelectDateListener() {
                    @Override
                    public void onSelectDate(int year, int month, int day, long time, int position) {
                        ToastUtils.showShortToast(getContext(), "year:" + year + "." + month + "." + day);
                    }
                }, mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay, Calendar.getInstance().get(Calendar.DAY_OF_WEEK)).show();
            }
            case R.id.llTitleDate: {
                resetMainTitleDate(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
            }
        }
    }

    private void resetMainTitleDate(int mCurrentSelectYear, int mCurrentSelectMonth, int mCurrentSelectDay) {
        TextView day = (TextView) searchViewById(R.id.tvTitleDay);
        TextView month = (TextView) searchViewById(R.id.tvTitleMonth);
        day.setText("" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        month.setText(getMonthName(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
    }

    private void showSelectDateDialog() {
        new SelectDateDialog(getMActivity(), this, mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay, slSchedule.getMonthCalendar().getCurrentItem()).show();
    }

    private void closeSoftInput() {
        etInputContent.clearFocus();
        DeviceUtils.closeSoftInput(getMActivity(), etInputContent);
    }

    private void addSchedule() {
        String content = etInputContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showShortToast(getMActivity(), R.string.schedule_input_content_is_no_null);
        } else {
            closeSoftInput();
            Schedule schedule = new Schedule();
            schedule.setTitle(content);
            schedule.setState(0);
            schedule.setTime(mTime);
            schedule.setYear(mCurrentSelectYear);
            schedule.setMonth(mCurrentSelectMonth);
            schedule.setDay(mCurrentSelectDay);
            new AddScheduleTask(getMActivity(), new OnTaskFinishedListener<Schedule>() {
                @Override
                public void onTaskFinished(Schedule data) {
                    if (data != null) {
                        mScheduleAdapter.insertItem(data);
                        etInputContent.getText().clear();
                        rLNoTask.setVisibility(View.GONE);
                        mTime = 0;
                        updateTaskHintUi(mScheduleAdapter.getItemCount() - 2);
                    }
                }
            }, schedule).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private void setCurrentSelectDate(int year, int month, int day) {
        mCurrentSelectYear = year;
        mCurrentSelectMonth = month;
        mCurrentSelectDay = day;
    }

    @Override
    public void onTaskFinished(List<Schedule> data) {
        mScheduleAdapter.changeAllData(data);
        rLNoTask.setVisibility(data.size() == 0 ? View.VISIBLE : View.GONE);
        updateTaskHintUi(data.size());
    }

    private void updateTaskHintUi(int size) {
        if (size == 0) {
            slSchedule.removeTaskHint(mCurrentSelectDay);
        } else {
            slSchedule.addTaskHint(mCurrentSelectDay);
        }
    }

    @Override
    public void onSelectDate(final int year, final int month, final int day, long time, int position) {
        slSchedule.getMonthCalendar().setCurrentItem(position);
        slSchedule.postDelayed(new Runnable() {
            @Override
            public void run() {
                slSchedule.getMonthCalendar().getCurrentMonthView().clickThisMonth(year, month, day);
            }
        }, 100);
        mTime = time;
    }

    public int getCurrentCalendarPosition() {
        return slSchedule.getMonthCalendar().getCurrentItem();
    }

    public String getMonthName(int idOfMonth) {
        switch (idOfMonth) {
            case (1): {
                return "Yanuar";
            }
            case (2): {
                return "Febural";
            }
            case (3): {
                return "March";
            }
            case (4): {
                return "April";
            }
            case (5): {
                return "May";
            }
            case (6): {
                return "July";
            }
            case (7): {
                return "June";
            }
            case (8): {
                return "August";
            }
            case (9): {
                return "September";
            }
            case (10): {
                return "October";
            }
            case (11): {
                return "November";
            }
            case (12): {
                return "December";
            }
            default:
                return "Not exists";
        }
    }
}
