package com.company.app.core.navigation

import android.os.Parcelable
import androidx.fragment.app.Fragment

/**
 * Created on 2019/01/27.
 */
interface FragmentNavigation {
    fun openMain(fragment: Fragment, arg: Parcelable? = null)
}