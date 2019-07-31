package com.example.myapplicatio.contacts

import android.os.Bundle
import uz.greenwhite.lib.mold.Mold
import uz.greenwhite.lib.mold.MoldContentFragment

class ContactsContentFragment : MoldContentFragment() {
    companion object {
        fun newInstance(): MoldContentFragment {
            var s = Bundle()
            s.putString("Dashboard", "Dashboard")
            return Mold.parcelableArgumentNewInstance(ContactsContentFragment::class.java, s)
        }
    }
}