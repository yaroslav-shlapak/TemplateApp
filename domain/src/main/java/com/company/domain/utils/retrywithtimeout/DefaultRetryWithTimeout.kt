package com.company.domain.utils.retrywithtimeout

import com.company.domain.utils.retries.FlowableRetry
import com.company.domain.utils.timeout.Timeout
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created on 2019/01/30.
 */
class DefaultRetryWithTimeout(
    private val flowableRetry: FlowableRetry,
    private val timeout: Timeout
) : RetryWithTimeout {

    override fun <T> flowableRetryWithTimeout(): (Flowable<T>) -> Flowable<T> =
            { upstream: Flowable<T> ->
                upstream
                        .retryWhen(flowableRetry)
                        .compose(timeout.flowableTimeout())
            }

    override fun <T> singleRetryWithTimeout(): (Single<T>) -> Single<T> =
            { upstream: Single<T> ->
                upstream
                        .toFlowable()
                        .compose(flowableRetryWithTimeout())
                        .firstOrError()
            }
}