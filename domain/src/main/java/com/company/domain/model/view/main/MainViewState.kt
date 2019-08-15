package com.company.domain.model.view.main

import com.company.domain.model.Failure
import com.company.domain.model.view.ViewState

/**
 * Created on 2019/01/25.
 */
sealed class MainViewState : ViewState {
    data class Error(val failure: Failure) : MainViewState()
    object Success : MainViewState()
}