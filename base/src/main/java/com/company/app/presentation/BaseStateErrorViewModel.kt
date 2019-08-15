package com.company.app.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.company.domain.model.view.ViewAction
import com.company.domain.model.view.ViewError
import com.company.domain.model.view.ViewState

import io.reactivex.disposables.CompositeDisposable

/**
 * Created on 3/22/18.
 */

abstract class BaseStateErrorViewModel<State : ViewState, Action : ViewAction, Error : ViewError>
protected constructor(
        protected val disposable: CompositeDisposable,
        protected val viewStateLiveData: MutableLiveData<State>,
        protected val viewErrorLiveData: MutableLiveData<Error>
) : BaseViewModel(disposable), BaseStateErrorViewModelContract<State, Action, Error> {
    override fun viewState(): LiveData<State> {
        return viewStateLiveData
    }

    override fun viewError(): LiveData<Error> {
        return viewErrorLiveData
    }
}
