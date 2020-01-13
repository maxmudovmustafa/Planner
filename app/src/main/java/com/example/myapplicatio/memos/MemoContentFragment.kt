package com.example.myapplicatio.memos

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.myapplicatio.BuildConfig
import com.example.myapplicatio.R
import com.example.myapplicatio.aralash.App
import com.example.myapplicatio.db.memo.MemoEntity
import com.example.myapplicatio.db.memo.MemoFactory
import com.example.myapplicatio.db.memo.MemoModelView
import com.example.myapplicatio.time_picker.TimePickerDialog
import com.example.myapplicatio.util.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.f_note.*
import kotlinx.android.synthetic.main.time_picker_legacy_material.view.*
import uz.greenwhite.lib.Tuple2
import uz.greenwhite.lib.collection.MyArray
import uz.greenwhite.lib.mold.Mold
import uz.greenwhite.lib.mold.MoldContentFragment
import uz.greenwhite.lib.view_setup.PopupBuilder
import uz.greenwhite.lib.view_setup.UI
import java.io.ByteArrayOutputStream
import java.io.File

class MemoContentFragment : MoldContentFragment(), View.OnClickListener {
    companion object {
        fun newInstance(): MoldContentFragment {
            var s = Bundle()
            s.putString("Dashboard", "Dashboard")
            return Mold.parcelableArgumentNewInstance(MemoContentFragment::class.java, s)
        }

        private const val TAG = "LocationProvider"
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.f_note, container, false)
    }

    private var doubleBackToExitPressedOnce: Long = 0
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    private var mLastLocation: Location? = null

    private var mLatitudeLabel: String? = null
    private var mLongitudeLabel: String? = null
    private var mLatitudeText: TextView? = null
    private var mLongitudeText: TextView? = null
    private var adapter: ImagesList? = null
    private var viewModel: MemoModelView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fMemo = MemoFactory(App.getApplicationContext(context!!))
        viewModel = ViewModelProviders.of(this, fMemo).get(MemoModelView::class.java)
        var memeos = viewModel!!.getAllMemo()
        if (memeos != null && memeos.isNotEmpty()) {
            showMessage(memeos[0].title)
        }

        tv_more_options.setOnClickListener(this)
        tv_time_start.setOnClickListener(this)
        tv_time_end.setOnClickListener(this)

        ll_all_day.setOnClickListener(this)
        ll_location.setOnClickListener(this)
        ll_add_member.setOnClickListener(this)
        ll_privacy.setOnClickListener(this)
        ll_attach_file.setOnClickListener(this)
        ll_color.setOnClickListener(this)

        btn_save.setOnClickListener(this)

        UI.makeDatePicker(tv_date_start)
        UI.makeDatePicker(et_date_end)
        tv_time_start.text = CalendarUtil.HOURS()
        tv_time_end.text = CalendarUtil.HOURS()

        mLatitudeLabel = "latitude_label"
        mLongitudeLabel = "longitude_label"
        mLatitudeText = latitude_text
        mLongitudeText = longitude_text

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)

        sw_all_day.setOnCheckedChangeListener { _, isChecked ->
            ViewUtil.visible(isChecked, ll_time_end)
            ViewUtil.visible(isChecked, tv_time_start)
        }

        rv_images.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun onClick(view: View?) {

        when (view!!.id) {
            R.id.tv_more_options -> {
                UI.popup().option(MyArray.from(optionPop()), object : PopupBuilder.CommandFacade<Tuple2> {
                    override fun apply(value: Tuple2?) {
                        tv_more_options.text = value!!.first as String
                    }

                    override fun getName(value: Tuple2?): CharSequence {
                        return value!!.first as String
                    }

                }).show(tv_more_options)
            }
            R.id.ll_all_day -> {
                ViewUtil.visible(sw_all_day.isChecked, ll_time_end)
                ViewUtil.visible(sw_all_day.isChecked, tv_time_start)
            }

            R.id.tv_time_start -> {
                setHour(tv_time_start)
            }
            R.id.tv_time_end -> {
                setHour(tv_time_end)
            }
            R.id.ll_location -> {

                var intent = Intent(context, LocationMap::class.java)
                startActivity(intent)
            }

            R.id.ll_add_member -> {
//                val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
                if(intent.resolveActivity(context?.packageManager)!=null)
                startActivityForResult(intent, 3)
            }

            R.id.ll_privacy -> {

            }

            R.id.ll_attach_file -> {
                openGallery()
            }

            R.id.ll_color -> {
                DialogColor(object : DialogColor.ColorPick {
                    override fun getColor(color: Drawable) {
                        ll_image_color.setImageDrawable(color)
                    }

                }).show(childFragmentManager, "color")

            }

            R.id.btn_save -> {
                if (!TextUtils.isEmpty(StringUtils.String(et_title))) {

                    ll_image_color.setBackgroundColor(resources.getColor(R.color.black))
                    var bitmap = ll_image_color.background as ColorDrawable
                    ll_image_color.setBackgroundColor(resources.getColor(R.color.white))

                    var items = if (adapter == null || adapter?.getItems() == null) {
                        ArrayList<ByteArray>()
                    } else adapter?.getItems()

                    viewModel!!.insertPeopel(MemoEntity(
                            StringUtils.String(et_title),
                            StringUtils.String(tv_date_start),
                            StringUtils.String(tv_time_start),
                            StringUtils.String(et_date_end),
                            StringUtils.String(tv_time_end),
                            true,
                            4,
                            456,
                            "people",
                            "people",
                            StringUtils.byteArrays(sha),
//                        StringUtils.String(note),
//                        items!!,
                            bitmap.color))
                    showMessage("Inserted all")
                }
            }
        }
    }

    private fun openGallery() {
        var intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "select"), 1)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.")
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> // Permission granted.
                    getLastLocation()
                else -> // Permission denied.
                    showSnackbar(R.string.not_available, R.string.setting,
                            View.OnClickListener {
                                // Build intent that displays the App settings screen.
                                val intent = Intent()
                                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                val uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null)
                                intent.data = uri
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            })
            }
        }
    }

    override fun onBackPressed(): Boolean {
        if (doubleBackToExitPressedOnce + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            return false
        } else showMessage("Press once again to exit!")
        doubleBackToExitPressedOnce = System.currentTimeMillis()
        return true
    }

    override fun onStart() {
        super.onStart()

        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLastLocation()
        }
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLastLocation() {
        mFusedLocationClient!!.lastLocation
                .addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful && task.result != null) {
                        mLastLocation = task.result

                        mLatitudeText!!.text = mLatitudeLabel + ":   " +
                                (mLastLocation)!!.latitude
                        mLongitudeText!!.text = mLongitudeLabel + ":   " +
                                (mLastLocation)!!.longitude
                    } else {
                        Log.w(TAG, "getLastLocation:exception", task.exception)
                        showMessage("Last location")
                    }
                }
    }

    private fun showMessage(text: String) {
        ToastUtils.showShortToast(context, text)
    }

    private fun showSnackbar(mainTextStringId: Int, actionStringId: Int,
                             listener: View.OnClickListener) {
        ToastUtils.showShortToast(context, getString(mainTextStringId))
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(activity!!,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity!!,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        if (shouldProvideRationale) {
            log("Displaying permission rationale to provide additional context.")

            showSnackbar(R.string.please_input_desc, android.R.string.ok,
                    View.OnClickListener {
                        startLocationPermissionRequest()
                    })

        } else {
            log("Requesting permission")
            startLocationPermissionRequest()
        }
    }

    private var sha: ByteArray? = null
    private var progress: ProgressDialog? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            var uri = data?.data
            progress = ProgressDialog.show(context, "Updating", "Please wait")
            if (requestCode == 34) {
//                var place = IntentBuilder.PlacePicker.getPlace(data, this)
//                var toastMsg = String.format("Place: %s", place.getName())
            } else if (requestCode == 1) {
                try {
                    var bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, uri)
                    val bytes = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                    sha = saveImage(bitmap)
                    rv_images.visibility = View.VISIBLE
                    if (adapter == null) makeAdapter(bitmap)
                    else adapter!!.addItem(Tuple2(bitmap, "Worked"))
                    showMessage("Updated picture")
                } catch (ex: Exception) {

                }
            } else if (requestCode == 3) {

                val projection: Array<String> = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
                if (context != null || uri != null) {

                    var cursor = activity?.contentResolver?.query(uri, projection, null, null, null)

                        if (cursor!!.moveToFirst()) {
                            val number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
//                            var name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
//                            showMessage("$number::$name")
                            showMessage("$number")
                            cursor.close()
                        }
                }
            }
            progress!!.dismiss()
        }
    }

    fun saveImage(bitmap: Bitmap): ByteArray? {
        val bytes = ByteArrayOutputStream()
        val wallpaperDirectory = File((Environment.getExternalStorageDirectory()).toString(),
                "my_app")
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdir()
        }
        try {
            val bytesSha = bytes.toByteArray()
//            val sha = Util.convertToHex(bytesSha)
//            val f = File(wallpaperDirectory, "$sha.tmp")

            return bytesSha
        } catch (ex: Exception) {
        }
        return null
    }

    fun makeAdapter(bitmap: Bitmap) {
        adapter = ImagesList(context!!, arrayListOf(Tuple2(bitmap, "worked")), object : ImagesList.ItemClickListener {
            override fun onItemClick(position: String) {
                showMessage(position)
            }
        })
        rv_images.adapter = adapter
    }

    fun log(s: String) {
        Log.i(TAG, s)
    }

    fun setHour(view: TextView) {
        TimePickerDialog(context!!,
                { listen, hourOfDay, minute ->
                    var mode = if (listen.amPm.value == 1) " pm" else " am"

                    view.text = String.format("%02d", hourOfDay) + ":" +
                            String.format("%02d", minute) + mode
                }
                , CalendarUtil.HOUR(),
                CalendarUtil.MINUTE(),
                DateFormat.is24HourFormat(context!!)).show()
    }

    private fun optionPop(): ArrayList<Tuple2> {
        return arrayListOf(Tuple2("Does not repeat", 1),
                Tuple2("Does not repeat", 1),
                Tuple2("Repeat every day", 2),
                Tuple2("Repeat every week", 3),
                Tuple2("Repeat every month", 4),
                Tuple2("Custom", 5))
    }

}