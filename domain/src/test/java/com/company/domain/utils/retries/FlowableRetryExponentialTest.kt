package com.company.domain.utils.retries

import com.company.domain.executor.ExecutionScheduler
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Flowable
import io.reactivex.schedulers.TestScheduler
import org.spekframework.spek2.Spek
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created on 2018/12/17.
 */
/** tests for
 * @see com.company.domain.utils.retries.FlowableRetryExponentialDefault
 **/
object FlowableRetryExponentialTest : Spek({

    val timeUnit = TimeUnit.SECONDS
    val numberOfRetries = 10

    val testScheduler: TestScheduler by memoized {
        TestScheduler()
    }

    val executionScheduler: ExecutionScheduler by memoized {
        mockk<ExecutionScheduler>(relaxed = true)
    }

    val retry: FlowableRetry by memoized {
        FlowableRetryExponential(
                numberOfRetries,
                executionScheduler
        )
    }

    test("fields check") {
        retry.maxRetries shouldBe numberOfRetries
        retry.executionScheduler shouldBe executionScheduler
    }

    beforeEachTest {
        every {
            executionScheduler.computation()
        } returns testScheduler
    }

    /** tests for
     * @see com.company.domain.utils.retries.FlowableRetryExponentialDefault.retryFunction
     **/
    group("exponential") {
        val attemptsAndResults = mapOf(
                0 to 0,
                1 to 0,
                2 to 1,
                3 to 2,
                5 to 8,
                8 to 64,
                13 to 2048,
                21 to 524288,
                10 to 256
        )
        attemptsAndResults.asSequence().forEach { (attempt, result) ->
            test("attempt: $attempt, result: $result") {
                retry.retryFunction(attempt) shouldBe result
            }

        }
    }

    group("retries") {
        val result = 123
        val atomicInteger: AtomicInteger by memoized {
            AtomicInteger(0)
        }

        test("9 errors before successful event") {
            val numberOfErrors = 10

            val source = Flowable
                    .fromCallable {
                        if (atomicInteger.incrementAndGet() < numberOfErrors) {
                            throw Exception()
                        } else {
                            result
                        }
                    }
                    .subscribeOn(testScheduler)


            val testObservable = source
                    .retryWhen(retry)
                    .subscribeOn(testScheduler)
                    .test()

            testScheduler.advanceTimeTo(10, timeUnit)
            testObservable
                    .assertNoErrors()
                    .assertNotComplete()
            testScheduler.advanceTimeTo(254, timeUnit)
            testObservable
                    .assertNoErrors()
                    .assertNotComplete()
            testScheduler.advanceTimeTo(256, timeUnit)
            testObservable
                    .assertNoErrors()
                    .assertComplete()
                    .assertValue { it == result }
        }

        test("4 errors before successful event") {
            val numberOfErrors = 4

            val source = Flowable
                    .fromCallable {
                        if (atomicInteger.incrementAndGet() < numberOfErrors) {
                            throw Exception()
                        } else {
                            result
                        }
                    }
                    .subscribeOn(testScheduler)


            val testObservable = source
                    .retryWhen(retry)
                    .subscribeOn(testScheduler)
                    .test()

            testScheduler.advanceTimeTo(1, timeUnit)
            testObservable
                    .assertNoErrors()
                    .assertNotComplete()
            testScheduler.advanceTimeTo(2, timeUnit)
            testObservable
                    .assertNoErrors()
                    .assertNotComplete()
            testScheduler.advanceTimeTo(5, timeUnit)
            testObservable
                    .assertNoErrors()
                    .assertComplete()
                    .assertValue { it == result }
        }

        test("no errors") {
            val numberOfErrors = 0

            val source = Flowable
                    .fromCallable {
                        if (atomicInteger.incrementAndGet() < numberOfErrors) {
                            throw Exception()
                        } else {
                            result
                        }
                    }
                    .subscribeOn(testScheduler)


            val testObservable = source
                    .retryWhen(retry)
                    .subscribeOn(testScheduler)
                    .test()

            testScheduler.advanceTimeTo(1, timeUnit)
            testObservable
                    .assertNoErrors()
                    .assertComplete()
                    .assertValue { it == result }
        }

        test("error after max retries") {
            val numberOfErrors = 11

            val source = Flowable
                    .fromCallable {
                        if (atomicInteger.incrementAndGet() < numberOfErrors) {
                            throw TimeoutException()
                        } else {
                            result
                        }
                    }
                    .subscribeOn(testScheduler)


            val testObservable = source
                    .retryWhen(retry)
                    .subscribeOn(testScheduler)
                    .test()

            testScheduler.advanceTimeTo(513, timeUnit)
            testObservable
                    .assertError(TimeoutException::class.java)
                    .assertNotComplete()
        }
    }
})