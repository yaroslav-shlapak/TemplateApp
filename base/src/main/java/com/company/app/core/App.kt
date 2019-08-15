package com.company.app.core

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDex
import com.company.app.di.components.DaggerAppComponent
import com.company.app.di.modules.AppModule
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.evernote.android.state.StateSaver
import com.facebook.stetho.Stetho
import com.google.firebase.FirebaseApp
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import dagger.android.support.HasSupportFragmentInjector
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import javax.inject.Inject

class App : Application(), HasSupportFragmentInjector, HasActivityInjector, HasServiceInjector {

    @Inject
    lateinit var dispatchingAndroidInjectorSupportFragment: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var dispatchingAndroidInjectorActivity: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var dispatchingAndroidInjectorService: DispatchingAndroidInjector<Service>

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        if (initLeakCanary()) return
        initDagger()
        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(TimberCrashlyticsTree())
        }
    }

    private fun initDagger() {
        DaggerAppComponent
            .builder()
            .application(this)
            .appModule(AppModule(this))
            .build()
            .inject(this)
    }

    private fun initLeakCanary(): Boolean {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return true
        }
        return false
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return dispatchingAndroidInjectorSupportFragment
    }

    override fun activityInjector(): AndroidInjector<Activity>? {
        return dispatchingAndroidInjectorActivity
    }

    override fun serviceInjector(): AndroidInjector<Service>? {
        return dispatchingAndroidInjectorService
    }

    companion object {

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}
