package com.company.domain.utils.retries

import androidx.annotation.VisibleForTesting
import com.company.domain.executor.ExecutionScheduler
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Created on 2/14/18.
 */

abstract class FlowableRetry(
        open val maxRetries: Int,
        open val executionScheduler: ExecutionScheduler
) : Function<Flowable<out Throwable>, Flowable<*>> {

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    abstract fun retryFunction(attempt: Int): Long

    override fun apply(attempts: Flowable<out Throwable>): Flowable<*> {
        return attempts
                .zipWith<Int, Flowable<Long>>(
                        Flowable.range(1, maxRetries),
                        BiFunction { err, attempt -> this.handleRetryAttempt(err, attempt) }
                )
                .flatMap { x -> x }
    }

    private fun handleRetryAttempt(err: Throwable, attempt: Int): Flowable<Long> {
        return when (attempt) {
            1 -> Flowable.just(42L)
            maxRetries -> Flowable.error(err)
            else -> {
                Timber.tag(TAG).i("retry attempt: $attempt after error: $err")
                Flowable.timer(retryFunction(attempt), TimeUnit.SECONDS, executionScheduler.computation())
            }
        }
    }

    companion object {
        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        const val DEFAULT_MAX_RETRIES = 10
        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        const val DELAY = 1 //seconds
        private const val TAG = "FlowableRetry2"
    }
}