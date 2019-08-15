package com.company.app.presentation.extensions

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

/**
 * Created on 2019/01/26.
 */
fun <T : ViewModel> FragmentActivity.viewModelOf(
    viewModelFactory: ViewModelProvider.Factory,
    clazz: Class<T>
): T {
    return ViewModelProviders.of(this, viewModelFactory).get<T>(clazz)
}

