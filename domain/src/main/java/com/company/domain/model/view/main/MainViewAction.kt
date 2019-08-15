package com.company.domain.model.view.main

import com.company.domain.model.view.ViewAction

/**
 * Created on 2019/03/18.
 */
sealed class MainViewAction : ViewAction {
    object DoSomeWork : MainViewAction()
}