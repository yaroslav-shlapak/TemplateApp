package com.company.app.presentation.main

import androidx.lifecycle.MutableLiveData
import com.company.app.presentation.BaseStateViewModel
import com.company.domain.model.view.main.MainViewAction
import com.company.domain.model.view.main.MainViewState
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

/**
 * Created on 2019/02/06.
 */
class MainFragmentViewModel
@Inject
constructor(

) : BaseStateViewModel<MainViewState, MainViewAction>(
    CompositeDisposable(),
    MutableLiveData()
) {
    override fun apply(action: MainViewAction) {
        when (action) {
            MainViewAction.DoSomeWork -> { }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("onCleared")
        viewStateLiveData.postValue(null)
    }

}