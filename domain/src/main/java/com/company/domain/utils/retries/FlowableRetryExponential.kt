package com.company.domain.utils.retries

import com.company.domain.executor.ExecutionScheduler

open class FlowableRetryExponential(
        override val maxRetries: Int,
        override val executionScheduler: ExecutionScheduler
) : FlowableRetry(
        maxRetries,
        executionScheduler
) {
    override fun retryFunction(attempt: Int): Long {
        return Math.pow(2.0, (attempt - 2).toDouble()).toLong()
    }
}