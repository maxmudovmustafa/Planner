package com.example.myapplicatio.dashboard

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.NavigationView
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.transition.Slide
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ToggleButton
import com.example.myapplicatio.R
import com.example.myapplicatio.aralash.App
import com.example.myapplicatio.aralash.UserViewFactory
import com.example.myapplicatio.calendar.DashboardFragment
import com.example.myapplicatio.db.user.UserEntity
import com.example.myapplicatio.db.user.UserModelView
import com.example.myapplicatio.memos.MemoContentFragment
import com.example.myapplicatio.reminder.ReminderContentFragment
import com.example.myapplicatio.util.ToastUtils
import com.example.myapplicatio.utils.Tab
import uz.greenwhite.lib.collection.MyArray
import uz.greenwhite.lib.mold.Mold
import uz.greenwhite.lib.mold.MoldContentFragment
import uz.greenwhite.lib.mold.NavigationFragment
import uz.greenwhite.lib.mold.NavigationItem
import uz.greenwhite.lib.view_setup.UI
import uz.greenwhite.lib.view_setup.ViewSetup
import java.io.ByteArrayOutputStream
import java.io.File


open class SessionIndexFragment : NavigationFragment() {
    companion object {
        fun open(activity: Activity, dialog: ProgressDialog) {
            Mold.openNavigation(activity, SessionIndexFragment::class.java)
            dialog.dismiss()

        }
    }

    private var allPeopleInfo: UserEntity? = null
    private var picture: Boolean? = true
    private var navigationView: NavigationView? = null
    private var viewModel: UserModelView? = null
    private var vsHeader: ViewSetup? = null
    private var progress: ProgressDialog? = null

    private val FORMS = MyArray.from<NavigationItem>(
            NavigationItem(Tab().CALENDAR, "Calendar", R.drawable.ic_calendar),
            NavigationItem(Tab().REMENDIR, "Reminder", R.drawable.ic_reminder),
            NavigationItem(Tab().NOTES, "Note", R.drawable.ic_note),
//            NavigationItem(Tab().BITHDAY, "Birthday", R.drawable.ic_fingerprint_black_48dp),
//            NavigationItem(Tab().SETTING, "Setting", R.drawable.ic_tools),
            NavigationItem(Tab().EXIT_APP, "Exit", R.drawable.ic_add_contact)
    )

    override fun showForm(form: NavigationItem?): Boolean {
        val f = make(form?.id)
        if (f != null) {
            Mold.replaceContent(activity, f, form)
            return true
        } else {

//            Mold.closeIndexDrawer(activity)
            return false
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reloadNavigationHeader()
    }

    override fun onDrawerOpened() {
        super.onDrawerOpened()
        reloadNavigationHeader()
        reloadIndexFragment()
    }

    protected fun reloadNavigationHeader() {
        val factory = UserViewFactory(App.getApplicationContext(context!!))
        viewModel = ViewModelProviders.of(this, factory).get(UserModelView::class.java)
        var allPeople = viewModel!!.getAllPeopleInfo()
        if (vsHeader == null) {
            vsHeader = ViewSetup(activity, R.layout.navigation_header)
            setNavigationHeader(vsHeader)
        }
        if (allPeople != null) {
            allPeopleInfo = allPeople[0]
            if (allPeopleInfo!!.name == "") {
                vsHeader!!.textView(R.id.tv_user_room_name).text = "UnKnowsn"
            } else {
                vsHeader!!.textView(R.id.tv_user_room_name).text = allPeopleInfo!!.name
            }

            val image = allPeopleInfo!!.getBitmap()
            val ll_layout = vsHeader!!.id<LinearLayout>(R.id.ll_detail)

            vsHeader!!.textView(R.id.et_user_name).text = allPeopleInfo!!.name
            vsHeader!!.editText(R.id.et_user_passw).setText(allPeopleInfo!!.password)
            vsHeader!!.textView(R.id.et_user_detail).text = allPeopleInfo!!.detail

            vsHeader!!.editText(R.id.et_user_passw).visibility = View.GONE
            if (image != null) {
                val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
                vsHeader!!.imageView(R.id.iv_avatar).setImageBitmap(bitmap)
            }
            vsHeader!!.imageView(R.id.miv_module_setting).setOnClickListener {
                makeImageChoose()
            }

            val swicher = vsHeader!!.compoundButton<ToggleButton>(R.id.img_expand)
            swicher.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    swicher.background = resources.getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp)
                    ll_layout.visibility = LinearLayout.VISIBLE

                } else {
//                    animateLayout(ll_layout)
                    swicher.background = resources.getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp)
                    ll_layout.visibility = LinearLayout.GONE
                }
            }
        }
    }

    fun animateLayout(ll_layout: LinearLayout) {
        var anim = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.ll_layout)
        anim.duration = 500
        ll_layout.animation = anim
        ll_layout.animate()
        anim.start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = UserViewFactory(App.getApplicationContext(context!!))
        viewModel = ViewModelProviders.of(this, factory).get(UserModelView::class.java)
        viewModel?.getLiveUsers()?.observe(this, Observer {
            allPeopleInfo = it!![0]
            reloadNavigationHeader()
        })

        if (allPeopleInfo != null) {

            val bitmap = BitmapFactory.decodeByteArray(allPeopleInfo!!.image!!, 0, allPeopleInfo!!.image!!.size)
            vsHeader?.imageView(R.id.iv_avatar)?.setImageBitmap(compressBitmap(bitmap, 500))
            picture = true
        }

    }

    private fun compressBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height

        var bitmapRatio = width as Float / height as Float
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio) as Int
        } else {
            height = maxSize
            width = (height * bitmapRatio) as Int
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    override fun onStart() {
        super.onStart()
        reloadIndexFragment()
    }

    override fun onShowDefaultForm() {
        super.onShowDefaultForm()

        Mold.replaceContent(activity, DashboardFragment.newInstance(), FORMS[0])
    }

    fun reloadIndexFragment() {
        navigationView = activity?.findViewById(com.example.myapplicatio.R.id.gwslib_navigation)
                as NavigationView
        setItems(FORMS)
    }


    fun make(id: Int?): MoldContentFragment? {
        when (id) {
            1 -> return DashboardFragment.newInstance()
            2 -> return ReminderContentFragment.newInstance()
            3 -> return MemoContentFragment.newInstance()
            4 -> {
//                var i = Intent(context, com.example.myapplicatio.dashboard.NavigationFragment::class.java)
//                startActivity(i)
            }
            5 -> {
            }
//                return SettingContentFragment.newInstance()
        }
        return null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            progress = ProgressDialog.show(context, "Updating", "Please wait")
            if (requestCode == 1) {
                var uri = data.data
                try {
                    var bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, uri)
                    val bytes = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                    val sha = saveImage(bitmap)
                    vsHeader!!.imageView(R.id.iv_avatar).setImageBitmap(bitmap)
                    allPeopleInfo!!.image = sha
                } catch (ex: Exception) {

                }
            } else {
                val thumbnail = data.extras.get("data") as Bitmap
                val bytes = ByteArrayOutputStream()
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                val sha = saveImage(thumbnail)
                vsHeader!!.imageView(R.id.iv_avatar).setImageBitmap(thumbnail)
                allPeopleInfo!!.image = sha
            }

            viewModel?.updateBitmap(allPeopleInfo!!)
            makeImageChoose()
            ToastUtils.showShortToast(context, "Updateed ficture")
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

    fun makeImageChoose() {
        var vs = ViewSetup(layoutInflater.inflate(R.layout.dialog_registration, null, false))
        var dialog = AlertDialog.Builder(context!!)
        var builder = Dialog(activity)
        val uName = vs.editText(R.id.ed_user_name)
        val uPassw = vs.editText(R.id.ed_user_passw)
        val uDetail = vs.editText(R.id.ed_user_detail)

        if (!isEmpty(allPeopleInfo!!.detail))
            uDetail.setText(allPeopleInfo!!.detail)
        uName.setText(allPeopleInfo!!.name)
        if (!isEmpty(allPeopleInfo!!.password))
            uPassw.setText(allPeopleInfo!!.password)
        if (allPeopleInfo!!.image != null) {
            val bitmap = BitmapFactory.decodeByteArray(allPeopleInfo!!.image, 0, allPeopleInfo!!.image!!.size)
            vs.imageView(R.id.iv_avatar).setImageBitmap(bitmap)
        }
        vs.imageView(R.id.iv_avatar).setOnClickListener {
            var items: Array<String> = Array(5) { "" }
            items[0] = "Gallery"
            items[1] = "Camera"
            with(dialog) {
                setTitle("Choose")
                setItems(items) { _, which ->
                    when (which) {
                        0 -> openGallery()
                        1 -> openCamera()
                    }
                }
            }
            dialog.create()
            dialog.show()
        }

        vs.compoundButton<ToggleButton>(R.id.tb_see_password).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                uPassw.transformationMethod = null
            else
                uPassw.transformationMethod = PasswordTransformationMethod()
        }

        vs.button(R.id.btn_cancel).setOnClickListener {
            builder.dismiss()
        }

        vs.button(R.id.btn_registr).setOnClickListener {
            if (!isEmpty(toString(uName))) {
                allPeopleInfo!!.name = toString(uName)
                allPeopleInfo!!.detail = toString(uDetail)
                allPeopleInfo!!.password = toString(uPassw)
                if (isEmpty(uPassw.toString()) || uPassw.toString().length == 4) {
                    allPeopleInfo!!.statePassword = true
                    viewModel!!.update(allPeopleInfo!!)
                    builder.dismiss()
                    reloadNavigationHeader()
                } else {
                    UI.alert(activity, "#numeric value", "Password should be 4 digit number")
                }
            } else {
                uName.error = "Filed is empty"
                uName.requestFocus()
            }
        }

        builder.setContentView(vs.view)
        builder.show()
    }


    fun toString(value: EditText): String {
        return value.text.toString()
    }

    fun isEmpty(value: String): Boolean {
        return TextUtils.isEmpty(value)
    }

    fun openGallery() {
        var intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "select"), 1)
    }

    fun openCamera() {
        var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 0)
    }

    fun setAnimation() {
        if (Build.VERSION.SDK_INT > 20) {
            var slide = Slide()
            slide.slideEdge = Gravity.LEFT
            slide.duration = 300
            slide.interpolator = DecelerateInterpolator()
            activity!!.window.exitTransition = slide
            activity!!.window.enterTransition = slide
        }
    }
}