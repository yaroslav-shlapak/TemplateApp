package com.company.app.presentation

import androidx.lifecycle.LiveData

/**
 * Created on 2019/01/26.
 */
interface BaseStateErrorViewModelContract<ViewState, ViewAction, ViewError>
    : BaseViewModelContract<ViewState, ViewAction> {
    fun viewError(): LiveData<ViewError>
}