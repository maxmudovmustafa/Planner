package com.example.myapplicatio.memos

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.myapplicatio.BuildConfig
import com.example.myapplicatio.R
import com.example.myapplicatio.time_picker.TimePickerDialog
import com.example.myapplicatio.util.CalendarUtil
import com.example.myapplicatio.util.ImagesList
import com.example.myapplicatio.util.ToastUtils
import com.example.myapplicatio.util.ViewUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.f_note.*
import kotlinx.android.synthetic.main.time_picker_legacy_material.view.*
import uz.greenwhite.lib.Tuple2
import uz.greenwhite.lib.collection.MyArray
import uz.greenwhite.lib.location.LocationHelper
import uz.greenwhite.lib.location.LocationResult
import uz.greenwhite.lib.mold.Mold
import uz.greenwhite.lib.mold.MoldContentFragment
import uz.greenwhite.lib.view_setup.PopupBuilder
import uz.greenwhite.lib.view_setup.UI
import uz.greenwhite.lib.view_setup.ViewSetup
import java.io.ByteArrayOutputStream
import java.io.File

class MemoContentFragment : MoldContentFragment() {
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

    //    private var viewModel: MemoModelView? = null
    private var doubleBackToExitPressedOnce: Long = 0
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    protected var mLastLocation: Location? = null

    private var mLatitudeLabel: String? = null
    private var mLongitudeLabel: String? = null
    private var mLatitudeText: TextView? = null
    private var mLongitudeText: TextView? = null
    private var adapter: ImagesList? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val calendar = Calendar.getInstance()
        var a = ArrayList<Tuple2>()
        a.add(Tuple2("Does not repeat", 1))
        a.add(Tuple2("Repeat every day", 2))
        a.add(Tuple2("Repeat every week", 3))
        a.add(Tuple2("Repeat every month", 4))
        a.add(Tuple2("Custom", 5))

        tv_more_options.setOnClickListener {
            UI.popup().option(MyArray.from(a), object : PopupBuilder.CommandFacade<Tuple2> {
                override fun apply(value: Tuple2?) {
                    tv_more_options.text = value!!.first as String
                }

                override fun getName(value: Tuple2?): CharSequence {
                    return value!!.first as String
                }

            }).show(tv_more_options)
        }
        mLatitudeLabel = "latitude_label"
        mLongitudeLabel = "longitude_label"
        mLatitudeText = latitude_text
        mLongitudeText = longitude_text

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)

//        val factory = MemoViewFactory(App.getApplicationContext(view.context))
//        viewModel = ViewModelProviders.of(this, factory).get(MemoModelView::class.java)

        sw_all_day.setOnCheckedChangeListener { _, isChecked ->
            ViewUtil.visible(isChecked, ll_time_end)
            ViewUtil.visible(isChecked, tv_time_start)
        }
        ll_all_day.setOnClickListener {
            ViewUtil.visible(sw_all_day.isChecked, ll_time_end)
            ViewUtil.visible(sw_all_day.isChecked, tv_time_start)
        }

        tv_time_start.setOnClickListener {
            setHour(tv_time_start)
        }

        tv_time_end.setOnClickListener {
            setHour(tv_time_end)
        }

        ll_location.setOnClickListener {


            var mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("google.streetview:cbll=46.414382,10.013988"))

            mapIntent.setPackage("com.google.android.apps.maps")
            startActivityForResult(mapIntent, 34)
//            var builder = PlacePicker.IntentBuilder()
//            startActivityForResult(builder.build(this), 3)
            getLastLocation()
            LocationHelper.getOneLocation(context, object : LocationResult() {
                override fun onLocationChanged(location: Location?) {
                    ToastUtils.showShortToast(context, "" + location?.latitude)
                }

            })
        }

        ll_add_member.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = ContactsContract.Contacts.CONTENT_TYPE
            startActivityForResult(intent, 3)
        }

        UI.makeDatePicker(tv_date_start)
        UI.makeDatePicker(et_date_end)
        tv_time_start.text = CalendarUtil.HOURS()
        tv_time_end.text = CalendarUtil.HOURS()

        ll_privacy.setOnClickListener {

        }

        ll_attach_file.setOnClickListener {
            openGallery()
        }


        ll_color.setOnClickListener {

            DialogColor(object : DialogColor.ColorPick {
                override fun getColor(color: Drawable) {
                    ll_image_color.background = color
                }

            }).show(childFragmentManager, "color")
        }
        btn_save.setOnClickListener {
            ToastUtils.showShortToast(context, "DOne")
        }

        rv_images.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
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
        } else ToastUtils.showLongToast(context, "Press once again to exit!")
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
                        showMessage("no_location_detected")
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

    private var progress: ProgressDialog? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            progress = ProgressDialog.show(context, "Updating", "Please wait")
            if (requestCode == 34) {
//                var place = IntentBuilder.PlacePicker.getPlace(data, this)
//                var toastMsg = String.format("Place: %s", place.getName())
                ToastUtils.showToast(context, "54555555555")
            } else if (requestCode == 1) {
                var uri = data!!.data
                try {
                    var bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, uri)
                    val bytes = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                    val sha = saveImage(bitmap)
                    rv_images.visibility = View.VISIBLE
                    if (adapter == null) makeAdapter(bitmap)
                    else adapter!!.addItem(Tuple2(bitmap, "Worked"))
                    sha
                    ToastUtils.showShortToast(context, "Updateed ficture")
                } catch (ex: Exception) {

                }
            } else if (requestCode == 3) {
                var contactUri = data!!.data
                val projection: Array<String> = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
                context?.contentResolver?.query(contactUri, projection, null, null, null).use { cursor ->
                    // If the cursor returned is valid, get the phone number
                    if (cursor?.moveToFirst()!!) {
                        val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        val number = cursor.getString(numberIndex)
                        ToastUtils.showToast(context!!, number)
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
                ToastUtils.showShortToast(context, position)
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
}