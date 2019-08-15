package com.company.domain.utils.retrywithtimeout

import com.company.domain.executor.ExecutionScheduler
import com.company.domain.utils.retries.FlowableRetry
import com.company.domain.utils.retries.FlowableRetryExponentialDefault
import com.company.domain.utils.timeout.DefaultTimeout
import com.company.domain.utils.timeout.DefaultTimeout.Companion.TIMEOUT_TIME
import com.company.domain.utils.timeout.Timeout
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.spekframework.spek2.Spek
import java.io.IOException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created on 2019/01/30.
 */
/** tests for
 * @see com.company.domain.utils.retrywithtimeout.DefaultRetryWithTimeout
 **/
object DefaultRetryWithTimeoutTest : Spek({
    val timeUnit = TimeUnit.SECONDS
    val result = 999

    val executionScheduler: ExecutionScheduler by memoized {
        mockk<ExecutionScheduler>(relaxed = true)
    }

    val testScheduler: TestScheduler by memoized {
        TestScheduler()
    }

    val finishedRetry: FlowableRetry by memoized {
        FlowableRetryExponentialDefault(
                executionScheduler
        )
    }

    val timeout: Timeout by memoized {
        DefaultTimeout(
                executionScheduler = executionScheduler
        )
    }

    val defaultRetryWithTimeout: DefaultRetryWithTimeout by memoized {
        DefaultRetryWithTimeout(
                finishedRetry,
                timeout
        )
    }


    beforeEachTest {
        every {
            executionScheduler.computation()
        } returns testScheduler

    }

    val atomicInteger: AtomicInteger by memoized {
        AtomicInteger(0)
    }

    /** tests for
     * @see com.company.domain.utils.retrywithtimeout.DefaultRetryWithTimeout.flowableRetryWithTimeout
     **/
    group("flowableRetryWithTimeout") {

        test("no timeout, no retries") {
            val flowable = Flowable
                    .fromCallable { result }
                    .delay(1, timeUnit, testScheduler)

            val testObserver = flowable
                    .compose(defaultRetryWithTimeout.flowableRetryWithTimeout())
                    .subscribeOn(testScheduler)
                    .test()

            testScheduler.advanceTimeTo(2, timeUnit)
            testObserver
                    .assertNoErrors()
                    .assertComplete()
                    .assertValues(result)
        }

        test("timeout, retries") {
            val numberOfErrors = 11

            val flowable = Flowable
                    .fromCallable {
                        if (atomicInteger.incrementAndGet() < numberOfErrors) {
                            throw IOException()
                        } else {
                            result
                        }
                    }
                    .subscribeOn(testScheduler)

            val testObserver = flowable
                    .compose(defaultRetryWithTimeout.flowableRetryWithTimeout())
                    .subscribeOn(testScheduler)
                    .test()

            testScheduler.advanceTimeTo(TIMEOUT_TIME, timeUnit)
            testObserver
                    .assertError(TimeoutException::class.java)
                    .assertNotComplete()
        }

    }
    /** tests for
     * @see com.company.domain.utils.retrywithtimeout.DefaultRetryWithTimeout.singleRetryWithTimeout
     **/
    group("singleRetryWithTimeout") {
        test("no timeout, no retries") {

            val single = Single
                    .fromCallable { result }
                    .delay(1, timeUnit, testScheduler)

            val testObserver = single
                    .compose(defaultRetryWithTimeout.singleRetryWithTimeout())
                    .subscribeOn(testScheduler)
                    .test()

            testScheduler.advanceTimeTo(2, timeUnit)
            testObserver
                    .assertNoErrors()
                    .assertComplete()
                    .assertValues(result)
        }

        test("timeout, retries") {
            val numberOfErrors = 5

            val single = Single
                    .fromCallable {
                        if (atomicInteger.incrementAndGet() < numberOfErrors) {
                            throw IOException()
                        } else {
                            result
                        }
                    }
                    .subscribeOn(testScheduler)

            val testObserver = single
                    .compose(defaultRetryWithTimeout.singleRetryWithTimeout())
                    .subscribeOn(testScheduler)
                    .test()

            testScheduler.advanceTimeTo(10, timeUnit)
            testObserver
                    .assertNoErrors()
                    .assertComplete()
                    .assertValues(result)
        }

        test("timeout, no retries") {
            val numberOfErrors = 11

            val single = Single
                    .fromCallable {
                        if (atomicInteger.incrementAndGet() < numberOfErrors) {
                            throw IOException()
                        } else {
                            result
                        }
                    }
                    .subscribeOn(testScheduler)

            val testObserver = single
                    .compose(defaultRetryWithTimeout.singleRetryWithTimeout())
                    .subscribeOn(testScheduler)
                    .test()

            testScheduler.advanceTimeTo(TIMEOUT_TIME, timeUnit)
            testObserver
                    .assertError(TimeoutException::class.java)
                    .assertNotComplete()
        }
    }
})