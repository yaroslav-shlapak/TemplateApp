package com.company.app.presentation

import androidx.lifecycle.LiveData

/**
 * Created on 2019/01/26.
 */
interface BaseViewModelContract<ViewState, ViewAction> {
    fun viewState(): LiveData<ViewState>
    fun apply(action: ViewAction)
}