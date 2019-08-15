package com.company.domain.utils.retrywithtimeout

import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created on 2019/01/30.
 */
interface RetryWithTimeout {
    fun <T> flowableRetryWithTimeout(): (Flowable<T>) -> Flowable<T>
    fun <T> singleRetryWithTimeout(): (Single<T>) -> Single<T>
}