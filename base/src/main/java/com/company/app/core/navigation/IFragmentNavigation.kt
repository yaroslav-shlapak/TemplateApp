package com.company.app.core.navigation

import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.company.app.R

/**
 * Created on 2019/01/24.
 */
class IFragmentNavigation : FragmentNavigation {
    override fun openMain(fragment: Fragment, arg: Parcelable?) {
        navigateTo(fragment, R.id.action_mainFragment_to_otherFragment, arg)
    }

    fun navigateTo(fragment: Fragment, @IdRes actionId: Int, arg: Parcelable? = null) {
        val bundle = arg?.let { Bundle().apply { putParcelable(BASE_FRAGMENT_ARG, it) } }
        NavHostFragment.findNavController(fragment).navigate(actionId, bundle)
    }
}

const val BASE_FRAGMENT_ARG = "BASE_FRAGMENT_ARG"