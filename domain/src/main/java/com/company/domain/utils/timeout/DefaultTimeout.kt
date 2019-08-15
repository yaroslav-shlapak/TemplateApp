package com.company.domain.utils.timeout

import com.company.domain.executor.ExecutionScheduler
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

/**
 * Created on 2019/01/30.
 */
class DefaultTimeout(
        private val timeoutTime: Long = TIMEOUT_TIME,
        private val timeoutTimeUnit: TimeUnit = TimeUnit.SECONDS,
        private val executionScheduler: ExecutionScheduler
) : Timeout {
    override fun timeoutTime() = timeoutTime

    override fun timeoutTimeUnit() = timeoutTimeUnit

    override fun scheduler() = executionScheduler.computation()

    override fun <T> flowableTimeout(): (Flowable<T>) -> Flowable<T> =
            { upstream: Flowable<T> ->
                upstream.timeout(timeoutTime(), timeoutTimeUnit(), scheduler())
            }

    companion object {
        const val TIMEOUT_TIME = 30L //s
    }
}
