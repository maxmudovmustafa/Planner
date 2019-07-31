package com.example.myapplicatio.reminder

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplicatio.R
import com.example.myapplicatio.aralash.App
import com.example.myapplicatio.aralash.UserViewFactory
import com.example.myapplicatio.db.ReminderEntity
import com.example.myapplicatio.db.ReminderModelView
import com.example.myapplicatio.time_picker.TimePickerDialog
import com.example.myapplicatio.util.ToastUtils
import com.example.myapplicatio.util.VoicePlayerView
import kotlinx.android.synthetic.main.f_reminder.*
import kotlinx.android.synthetic.main.time_picker_legacy_material.view.*
import uz.greenwhite.lib.Tuple2
import uz.greenwhite.lib.collection.MyArray
import uz.greenwhite.lib.mold.Mold
import uz.greenwhite.lib.mold.MoldContentFragment
import uz.greenwhite.lib.view_setup.PopupBuilder
import uz.greenwhite.lib.view_setup.UI
import java.io.File
import java.util.*


class ReminderContentFragment : MoldContentFragment(), Click_Reminder {
//        , TimePickerDialog.OnTimeSetListener {
//    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
//        ToastUtils.showShortToast(context, "" + hourOfDay)
//    }

    companion object {
        fun newInstance(): MoldContentFragment? {
            var s = Bundle()
            s.putString("Dashboard", "Dashboard")
            return Mold.parcelableArgumentNewInstance(ReminderContentFragment::class.java, s)
        }
    }

    private var player: VoicePlayerView? = null
    private var selectedNumber: Int = 1
    private var viewModel: ReminderModelView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.f_reminder, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val calendar = Calendar.getInstance()
        var a = ArrayList<Tuple2>()
        a.add(Tuple2("Does not repeat", 1))
        a.add(Tuple2("Repeat every day", 2))
        a.add(Tuple2("Repeat every week", 3))
        a.add(Tuple2("Repeat every month", 4))
        a.add(Tuple2("Custom", 5))

        val factory2 = UserViewFactory(App.getApplicationContext(view.context))
        viewModel = ViewModelProviders.of(this, factory2).get(ReminderModelView::class.java)
        var string = StringBuilder()
        viewModel?.getAllPeopleInfo()?.value?.forEach {
            string.append(it.title)
            string.append(it.date)
            string.append(it.time)
        }
        string.append("")
        ToastUtils.showShortToast(context, string.toString())
        player = voicePlayerView as VoicePlayerView
        checkPermissions()
        player?.setAudio(Environment.getExternalStorageDirectory().path + File.separator + "song.mp3")

        sw_all_day.setOnCheckedChangeListener { _, isChecked ->
            makeVisiblity(isChecked)
        }

        ll_all_day.setOnClickListener {
            makeVisiblity(sw_all_day.isChecked)
        }

        tv_time_start.setOnClickListener {
            var mode: String = ""
            TimePickerDialog(context!!,
                    { listen, hourOfDay, minute ->
                        if (listen.amPm.value == 1) {
                            mode = " pm"
                        } else {
                            mode = " am"
                        }
                        tv_time_start.text = String.format("%02d", hourOfDay) + ":" +
                                String.format("%02d", minute) +
                                mode
                    }
                    , calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(context!!)).show()
        }

        UI.makeDatePicker(ed_date_start)
        ed_date_start.setOnClickListener {

        }

        btn_save.setOnClickListener {
            viewModel?.insert(ReminderEntity(
                    et_title.text.toString(),
                    ed_date_start.text.toString(),
                    tv_time_start.text.toString(),
                    true,
                    Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
            ))
            ToastUtils.showShortToast(context, "DOne")
        }
        ll_reminder_repeat.setOnClickListener {
            UI.popup().option(MyArray.from(a), object : PopupBuilder.CommandFacade<Tuple2> {
                override fun getName(name: Tuple2?): CharSequence {
                    return name?.first.toString()
                }

                override fun apply(name: Tuple2?) {
                    tv_repeat_message.text = name?.first as String
                }

            }).show(ll_reminder_repeat)
//            RepeatFragment.openContent(this, childFragmentManager, selectedNumber)
        }
    }


    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(view!!.context,
                        Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!,
                            Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(activity!!,
                        arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        100)

                // MY_PERMISSIONS_REQUEST_READ_WRITE_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            100 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request.
    }

    override fun onClickValue(value: Int, message: String) {
        ToastUtils.showShortToast(context, message)
        tv_repeat_message.text = message
        selectedNumber = value
    }

    override fun onStop() {
        super.onStop()
        player?.onStop()
    }

    override fun onPause() {
        super.onPause()
        player?.onPause()
    }

    fun makeVisiblity(status: Boolean) {
        if (status)
            tv_time_start.visibility = View.INVISIBLE
        else
            tv_time_start.visibility = View.VISIBLE
    }

    private var doubleBackToExitPressedOnce: Long = 0
    override fun onBackPressed(): Boolean {
        if (doubleBackToExitPressedOnce + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            return false
        } else ToastUtils.showLongToast(context, "Press once again to exit!")
        doubleBackToExitPressedOnce = System.currentTimeMillis()
        return true
    }
}