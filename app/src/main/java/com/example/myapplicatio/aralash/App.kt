package com.example.myapplicatio.aralash

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho


class App : Application()
//        HasActivityInjector
{
    companion object {
        fun getApplicationContext(context: Context) = context.applicationContext as App
    }

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)

        /*DaggerExtendedComponent.builder().application(this).build().inject(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }*/
    }

    /*@Inject
    var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>? = null

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityDispatchingAndroidInjector!!
    }*/

}