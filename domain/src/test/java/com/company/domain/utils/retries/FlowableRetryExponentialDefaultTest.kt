package com.company.domain.usecase.usecase.utils.retries

import com.company.domain.executor.ExecutionScheduler
import com.company.domain.utils.retries.FlowableRetry
import com.company.domain.utils.retries.FlowableRetryExponentialDefault
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.reactivex.schedulers.TestScheduler
import org.spekframework.spek2.Spek

/**
 * Created on 2019/01/30.
 */
/** tests for
 * @see com.metricinsights.mobile.android.domain.utils.retries2.FlowableRetryExponentialDefault
 **/
object FlowableRetryExponentialDefaultTest : Spek({

    val numberOfRetries = FlowableRetry.DEFAULT_MAX_RETRIES

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

    val retry: FlowableRetry by memoized {
        FlowableRetryExponentialDefault(
                executionScheduler
        )
    }

    test("fields check") {
        retry.maxRetries shouldBe numberOfRetries
        retry.executionScheduler shouldBe executionScheduler
    }
})