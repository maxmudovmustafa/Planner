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
import android.text.method.HideReturnsTransformationMethod
import android.transition.Slide
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.ToggleButton
import com.example.myapplicatio.R
import com.example.myapplicatio.aralash.App
import com.example.myapplicatio.aralash.UserViewFactory
import com.example.myapplicatio.calendar.DashboardFragment
import com.example.myapplicatio.db.UserEntity
import com.example.myapplicatio.db.UserModelView
import com.example.myapplicatio.memos.MemoContentFragment
import com.example.myapplicatio.reminder.ReminderContentFragment
import com.example.myapplicatio.util.ToastUtils
import com.example.myapplicatio.utils.Tab
import com.google.android.gms.common.api.internal.LifecycleCallback
import com.google.android.gms.common.api.internal.LifecycleFragment
import uz.greenwhite.lib.collection.MyArray
import uz.greenwhite.lib.mold.Mold
import uz.greenwhite.lib.mold.MoldContentFragment
import uz.greenwhite.lib.mold.NavigationFragment
import uz.greenwhite.lib.mold.NavigationItem
import uz.greenwhite.lib.view_setup.UI
import uz.greenwhite.lib.view_setup.ViewSetup
import java.io.ByteArrayOutputStream
import java.io.File


open class SessionIndexFragment : NavigationFragment(), LifecycleFragment {
    override fun <T : LifecycleCallback?> getCallbackOrNull(p0: String?, p1: Class<T>?): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addCallback(p0: String?, p1: LifecycleCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isCreated(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isStarted(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLifecycleActivity(): Activity {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        fun open(activity: Activity, dialog: ProgressDialog) {
            Mold.openNavigation(activity, SessionIndexFragment::class.java)
            dialog.dismiss()

        }
    }

    private var allPeopleInfo: List<UserEntity>? = null
    private var picture: Boolean? = true
    private var navigationView: NavigationView? = null
    private var viewModel: UserModelView? = null
    private var vsHeader: ViewSetup? = null
    private var vs: ViewSetup? = null
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
        allPeopleInfo = viewModel!!.getAllPeopleInfo()
        if (vsHeader == null) {
            vsHeader = ViewSetup(activity, R.layout.navigation_header)
            setNavigationHeader(vsHeader)
        }
        if (allPeopleInfo != null) {
            if (allPeopleInfo!![0].name == "") {
                vsHeader!!.textView(R.id.tv_user_room_name).text = "UnKnowsn"
            } else {
                vsHeader!!.textView(R.id.tv_user_room_name).text = allPeopleInfo!![0].name
            }

            val image = allPeopleInfo!![0].getBitmap()
            val ll_layout = vsHeader!!.id<LinearLayout>(R.id.ll_detail)

            vsHeader!!.editText(R.id.et_user_name).setText(allPeopleInfo!![0].name)
            vsHeader!!.editText(R.id.et_user_passw).setText(allPeopleInfo!![0].password)
            vsHeader!!.editText(R.id.et_user_detail).setText(allPeopleInfo!![0].detail)

            vsHeader!!.editText(R.id.et_user_passw).visibility = View.GONE
            if (image != null) {
                val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
                vsHeader!!.imageView(R.id.iv_avatar).setImageBitmap(bitmap)
            }
            vsHeader!!.imageView(R.id.miv_module_setting).setOnClickListener {
                if (picture!!) makeImageChoose()
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
            allPeopleInfo = it
            reloadNavigationHeader()
        })

        if (allPeopleInfo != null) {

            val bitmap = BitmapFactory.decodeByteArray(allPeopleInfo!![0].image!!, 0, allPeopleInfo!![0].image!!.size)
            vsHeader?.imageView(R.id.iv_avatar)?.setImageBitmap(bitmap)
            picture = true
        }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addMenu("Search", ViewSetup(context, R.layout.abc_search_dropdown_item_icons_2line).view)
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
            if (requestCode == 1) {
                var uri = data.data
                try {
                    var bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, uri)
                    val bytes = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
                    val sha = saveImage(bitmap)
                    vsHeader!!.imageView(R.id.iv_avatar).setImageBitmap(bitmap)
                    allPeopleInfo!![0].image = sha
                } catch (ex: Exception) {

                }
            } else {
                val thumbnail = data.extras.get("data") as Bitmap
                val bytes = ByteArrayOutputStream()
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
                val sha = saveImage(thumbnail)
                vsHeader!!.imageView(R.id.iv_avatar).setImageBitmap(thumbnail)
                allPeopleInfo!![0].image = sha
            }

            viewModel?.updateBitmap(allPeopleInfo!![0])
            ToastUtils.showShortToast(context, "Updateed ficture")
        }
    }

    fun saveImage(bitmap: Bitmap): ByteArray? {
        val bytes = ByteArrayOutputStream()
        val wallpaperDirectory = File((Environment.getExternalStorageDirectory()).toString(),
                "my_app")
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
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
        var dialog = AlertDialog.Builder(context!!)
        var builder = Dialog(activity)
        vs = ViewSetup(layoutInflater.inflate(R.layout.dialog_registration, null, false))
        if (!TextUtils.isEmpty(allPeopleInfo!![0].detail))
            vs!!.editText(R.id.ed_user_detail).setText(allPeopleInfo!![0].detail)
        vs!!.editText(R.id.ed_user_name).setText(allPeopleInfo!![0].name)
        if (!TextUtils.isEmpty(allPeopleInfo!![0].password))
            vs?.editText(R.id.ed_user_passw)?.setText(allPeopleInfo!![0].password)
        if (allPeopleInfo!![0].image != null) {
            val bitmap = BitmapFactory.decodeByteArray(allPeopleInfo!![0].image, 0, allPeopleInfo!![0].image!!.size)
            vs!!.imageView(R.id.iv_avatar).setImageBitmap(bitmap)
        }
        vs!!.imageView(R.id.iv_avatar).setOnClickListener {
            var items: Array<String> = Array(5) { "" }
            items[0] = "Gallery"
            items[1] = "Camera"
            with(dialog) {
                setTitle("Choose")
                setItems(items) { _, which ->
                    when (which) {
                        0 -> chooseGallery()
                        1 -> fromGalley()
                    }
                }
            }
            dialog.create()
            dialog.show()
        }

        vs!!.button(R.id.btn_registr).setOnClickListener {
            val name = vs!!.editText(R.id.ed_user_name).text.toString()
            val passw = vs!!.editText(R.id.ed_user_passw).text.toString()
            val detail = vs!!.editText(R.id.ed_user_detail).text.toString()
            /*          val image = vs.imageView(R.id.iv_avatar).drawable
                      val bitmapDrawable = image as BitmapDrawable
                      val bitmap = bitmapDrawable.bitmap
                      var stream = ByteArrayOutputStream()
                      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                      var s = stream.toByteArray()
          */
            val userEntity = UserEntity(name, passw, detail)

            vs!!.compoundButton<ToggleButton>(R.id.tb_see_password).setOnCheckedChangeListener { _, isChecked ->
                if (isChecked)
                    vs!!.editText(R.id.ed_user_passw).transformationMethod = HideReturnsTransformationMethod.getInstance()
                else
                    vs!!.editText(R.id.ed_user_passw).transformationMethod = HideReturnsTransformationMethod.getInstance()
            }

            vs!!.button(R.id.btn_cancel).setOnClickListener {
                builder.dismiss()
            }

            if (passw.isEmpty() || passw.length == 4) {
                if (!TextUtils.isEmpty(userEntity.password)) {
                    userEntity.statePassword = true
                }
                builder.dismiss()
            } else {
                UI.alert(activity, "#numeric value", "Password should be 4 digit number")
            }
            userEntity.setBitmap(allPeopleInfo!![0].image!!)
            viewModel!!.updatePeople(userEntity, allPeopleInfo!![0].id)
            reloadIndexFragment()
        }

        builder.setContentView(vs!!.view)
        builder.show()
    }


    fun chooseGallery() {
        var intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "select"), 1)
    }

    fun fromGalley() {
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