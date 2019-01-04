package com.company.app.core

import android.app.Activity
import android.app.Application
import android.os.Bundle
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicInteger

class AppState(
    app: Application,
    private val appStateListener: AppStateListener?
) : Application.ActivityLifecycleCallbacks {

    @Volatile
    private var background = true
    private val currentActivities: AtomicInteger
    private var topActivityWeakReference: WeakReference<Activity>? = null


    val topActivity: Activity?
        get() = if (!background && topActivityWeakReference?.get() != null
                && topActivityWeakReference?.get()?.isFinishing == false) {
            topActivityWeakReference?.get()
        } else {
            null
        }

    init {
        app.registerActivityLifecycleCallbacks(this)
        this.currentActivities = AtomicInteger(0)
    }

    override fun onActivityResumed(activity: Activity?) {
        topActivityWeakReference = WeakReference<Activity>(activity)
        if (activity != null) {
            Timber.d("activity name: %s",
                    activity.javaClass.simpleName)
        }
        val num = currentActivities.incrementAndGet()
        Timber.d("AppState inc num: %s", num)
        if (background) {
            background = false
            Timber.d("Foreground!")
            if (appStateListener != null) {
                try {
                    appStateListener.onForeground()
                } catch (e: Exception) {
                    Timber.e("listener ${appStateListener.javaClass.signers} produces error: $e")
                }

            } else {
                Timber.e("listener is null!")
            }
        }
    }


    override fun onActivityStopped(activity: Activity) {
        if (currentActivities.get() == 0) {
            background = true
            Timber.d("Background!")
            if (appStateListener != null) {
                try {
                    appStateListener.onBackground()
                } catch (e: Exception) {
                    Timber.e("listener ${appStateListener.javaClass.signers} produces error: $e")
                }

            } else {
                Timber.e("listener is null!")
            }
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {
        val num = currentActivities.decrementAndGet()
        Timber.d("AppState dec num: %s", num)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}


    interface AppStateListener {
        fun onForeground()
        fun onBackground()
    }
}
