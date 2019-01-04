package com.company.app.core

import android.util.Log
import com.crashlytics.android.Crashlytics
import timber.log.Timber

class TimberCrashlyticsTree : Timber.Tree() {

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return priority == Log.ERROR || priority == Log.WARN
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (t != null) {
            Crashlytics.logException(t)
        } else {
            Crashlytics.log(priority, tag, message)
        }
    }
}
