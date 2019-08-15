package com.company.app.presentation

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

/**
 * Created on 3/22/18.
 */

abstract class BaseViewModel protected constructor(
    protected val compositeDisposable: CompositeDisposable
) : ViewModel() {

    protected fun addDisposable(disposable: Disposable?) {
        disposable?.let {
            compositeDisposable.add(disposable)
        }
    }

    protected fun clearDisposables() {
        compositeDisposable.clear()
    }

    override fun onCleared() {
        super.onCleared()
        clearDisposables()
        Timber.tag(TAG).i("onCleared")
    }

    companion object {
        private const val TAG: String = "BaseViewModel"
    }
}
