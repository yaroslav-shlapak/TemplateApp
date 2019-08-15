package com.company.app.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.company.domain.model.view.ViewAction
import com.company.domain.model.view.ViewState
import io.reactivex.disposables.CompositeDisposable

/**
 * Created on 3/22/18.
 */

abstract class BaseStateViewModel<State : ViewState, Action : ViewAction>
protected constructor(
        protected val disposable: CompositeDisposable,
        protected val viewStateLiveData: MutableLiveData<State>
) : BaseViewModel(disposable), BaseViewModelContract<State, Action> {
    override fun viewState(): LiveData<State> {
        return viewStateLiveData
    }
}
