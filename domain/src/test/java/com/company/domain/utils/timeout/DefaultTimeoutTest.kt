package com.company.domain.utils.timeout

import com.company.domain.executor.ExecutionScheduler
import com.company.domain.utils.timeout.DefaultTimeout.Companion.TIMEOUT_TIME
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Flowable
import io.reactivex.schedulers.TestScheduler
import org.spekframework.spek2.Spek
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Created on 2019/01/30.
 */
/** tests for
 * @see com.company.domain.utils.timeout.DefaultTimeout
 **/
object DefaultTimeoutTest : Spek({
    val executionScheduler: ExecutionScheduler by memoized {
        mockk<ExecutionScheduler>(relaxed = true)
    }
    val testScheduler: TestScheduler by memoized {
        TestScheduler()
    }
    beforeEachTest {
        every {
            executionScheduler.computation()
        } returns testScheduler
    }

    group("construct") {
        test("all parameters") {
            val timeoutTime = 100L
            val timeoutTimeUnit: TimeUnit = TimeUnit.MINUTES
            val defaultTimeout = DefaultTimeout(
                    timeoutTime,
                    timeoutTimeUnit,
                    executionScheduler
            )

            defaultTimeout.scheduler() shouldBe testScheduler
            defaultTimeout.timeoutTimeUnit() shouldBe timeoutTimeUnit
            defaultTimeout.timeoutTime() shouldBe timeoutTime
        }

        test("single parameter") {
            val timeoutTime = TIMEOUT_TIME
            val timeoutTimeUnit: TimeUnit = TimeUnit.SECONDS
            val defaultTimeout = DefaultTimeout(
                    executionScheduler = executionScheduler
            )

            defaultTimeout.scheduler() shouldBe testScheduler
            defaultTimeout.timeoutTimeUnit() shouldBe timeoutTimeUnit
            defaultTimeout.timeoutTime() shouldBe timeoutTime
        }

        test("executionScheduler and timeoutTime") {
            val timeoutTime = 100L
            val timeoutTimeUnit: TimeUnit = TimeUnit.SECONDS
            val defaultTimeout = DefaultTimeout(
                    timeoutTime = timeoutTime,
                    executionScheduler = executionScheduler
            )

            defaultTimeout.scheduler() shouldBe testScheduler
            defaultTimeout.timeoutTimeUnit() shouldBe timeoutTimeUnit
            defaultTimeout.timeoutTime() shouldBe timeoutTime
        }

        test("executionScheduler and timeoutTimeUnit") {
            val timeoutTime = TIMEOUT_TIME
            val timeoutTimeUnit: TimeUnit = TimeUnit.HOURS
            val defaultTimeout = DefaultTimeout(
                    timeoutTimeUnit = timeoutTimeUnit,
                    executionScheduler = executionScheduler
            )

            defaultTimeout.scheduler() shouldBe testScheduler
            defaultTimeout.timeoutTimeUnit() shouldBe timeoutTimeUnit
            defaultTimeout.timeoutTime() shouldBe timeoutTime
        }
    }

    /** tests for
     * @see com.company.domain.utils.timeout.DefaultTimeout.flowableTimeout
     **/
    group("flowableTimeout") {
        val timeouts = listOf<Triple<Long, Long, Long>>(
                Triple(1, 99, 100),
                Triple(1, 9, 10),
                Triple(10, 999, 1000),
                Triple(1000, 10000L - 1L, 10000L)
        )
        val timeunits = listOf(
                TimeUnit.DAYS,
                TimeUnit.HOURS,
                TimeUnit.HOURS,
                TimeUnit.MINUTES,
                TimeUnit.SECONDS,
                TimeUnit.MILLISECONDS,
                TimeUnit.MICROSECONDS
        )

        timeunits.asSequence().forEach { timeunit ->
            timeouts.asSequence().forEach { (first, second, last) ->
                test("timeunit: $timeunit, first: $first, second: $second, last: $last") {
                    val defaultTimeout = DefaultTimeout(
                            last,
                            timeunit,
                            executionScheduler
                    )
                    val flowable = Flowable
                            .fromCallable { }
                            .delay(Long.MAX_VALUE, timeunit, testScheduler)

                    val testObserver = flowable
                            .compose(defaultTimeout.flowableTimeout())
                            .subscribeOn(testScheduler)
                            .test()

                    testScheduler.advanceTimeTo(first, timeunit)
                    testObserver
                            .assertNoErrors()
                            .assertNotComplete()

                    testScheduler.advanceTimeTo(second, timeunit)
                    testObserver
                            .assertNoErrors()
                            .assertNotComplete()

                    testScheduler.advanceTimeTo(last, timeunit)
                    testObserver
                            .assertError(TimeoutException::class.java)
                            .assertNotComplete()
                }
            }
        }
    }

})