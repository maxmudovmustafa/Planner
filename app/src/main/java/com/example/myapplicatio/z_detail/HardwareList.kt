package com.example.myapplicatio.z_detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myapplicatio.R
import uz.greenwhite.lib.mold.Mold
import uz.greenwhite.lib.mold.MoldContentFragment
import uz.greenwhite.lib.view_setup.ViewSetup
import java.io.IOException

class HardwareList : MoldContentFragment() {
    companion object {
        fun open(activity: Activity) {
            Mold.openContent(activity, HardwareList::class.java)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        vs = ViewSetup(inflater.inflate(R.layout.list_info, container, false))
        return vs!!.view
    }

    var vs: ViewSetup? = null
    @SuppressLint("HardwareIds")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var items = ArrayList<MyTextSample>()

        val arch = System.getProperty("os.arch")
        var device = resources.getStringArray(R.array.device_info)
        var soc = resources.getStringArray(R.array.soc)
        var cpu = resources.getStringArray(R.array.cpu)
        var memory = resources.getStringArray(R.array.memory)
        var os = resources.getStringArray(R.array.os)
        var radio = resources.getStringArray(R.array.radio)
        var sensor = resources.getStringArray(R.array.sensor)

        items.add(MyTextSample(device.get(0), Build.MODEL))
        items.add(MyTextSample(device.get(1), Build.BOARD))
        items.add(MyTextSample(device.get(2), Build.BRAND))
        items.add(MyTextSample(device.get(3), Build.MANUFACTURER))
        items.add(MyTextSample(device.get(4), Build.DEVICE))
        items.add(MyTextSample(device.get(5), Build.PRODUCT))
        items.add(MyTextSample(device.get(6), Build.TAGS))
        items.add(MyTextSample(device.get(7), Build.DISPLAY))

        items.add(MyTextSample(soc.get(0), Build.HARDWARE))
        items.add(MyTextSample(soc.get(1), " " + getNumberOfCores()))
        items.add(MyTextSample(soc.get(2), arch))

        items.add(MyTextSample(cpu.get(0), ReadCPUinfo()))

        items.add(MyTextSample(memory.get(0), getMemoryInfo()))

        items.add(MyTextSample(os.get(0), Build.VERSION.RELEASE))
        items.add(MyTextSample(os.get(1), Build.VERSION.INCREMENTAL))
        items.add(MyTextSample(os.get(2), Build.VERSION.BASE_OS))
        items.add(MyTextSample(os.get(3), Build.VERSION.CODENAME))
        items.add(MyTextSample(os.get(4), Build.VERSION.SECURITY_PATCH))
        items.add(MyTextSample(os.get(5), "" + Build.VERSION.PREVIEW_SDK_INT))
        items.add(MyTextSample(os.get(6), "" + Build.VERSION.SDK_INT))
        items.add(MyTextSample(os.get(7), Build.DISPLAY))
        items.add(MyTextSample(os.get(8), Build.FINGERPRINT))
        items.add(MyTextSample(os.get(9), Build.ID))
        items.add(MyTextSample(os.get(10), Build.TYPE))
        items.add(MyTextSample(os.get(11), Build.USER))
        items.add(MyTextSample(os.get(12), Build.BOOTLOADER))
        items.add(MyTextSample(os.get(13), System.getProperty("os.version")))

        items.add(MyTextSample(radio.get(0), Build.getRadioVersion()))

        var sensor_name = StringBuilder()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var sm = activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager?
            val sensor_list = sm!!.getSensorList(Sensor.TYPE_ALL)

            sensor_list.forEach {
                sensor_name.append(it.name + "\n")
            }
            items.add(MyTextSample(sensor.get(0), Build.getRadioVersion()))
        }

        var adapter = HardwareAdapter(context!!, items, object : HardwareAdapter.ItemClickListener {
            override fun onItemClick(position: MyTextSample) {
                Toast.makeText(context, position.detail, Toast.LENGTH_SHORT).show()
            }
        })
        vs!!.id<RecyclerView>(R.id.rc_view).layoutManager = LinearLayoutManager(context)
        vs!!.id<RecyclerView>(R.id.rc_view).adapter = adapter
    }

    fun getMemoryInfo(): String {
        val cmd: ProcessBuilder
        var result = ""

        try {
            val args = arrayOf("/system/bin/cat", "/proc/meminfo")
            cmd = ProcessBuilder(*args)

            val process = cmd.start()
            val `in` = process.inputStream
            val re = ByteArray(1024)
            while (`in`.read(re) != -1) {
                println(String(re))
                result = result + String(re)
            }
            `in`.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return result
    }


    private fun ReadCPUinfo(): String {
        val cmd: ProcessBuilder
        var result = ""

        try {
            val args = arrayOf("/system/bin/cat", "/proc/cpuinfo")
            cmd = ProcessBuilder(*args)

            val process = cmd.start()
            val `in` = process.inputStream
            val re = ByteArray(1024)
            while (`in`.read(re) != -1) {
                println(String(re))
                result = result + String(re)
            }
            `in`.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return result
    }

    fun getNumberOfCores(): Int {
        return if (Build.VERSION.SDK_INT >= 17) {
            Runtime.getRuntime().availableProcessors()
        } else {
            //Code for old SDK values
            0
            //return Runtime.getRuntime().availableProcessors();
        }
    }
    /*
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        }
        return manufacturer + " " + model;
    }
    */
}