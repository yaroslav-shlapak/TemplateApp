package com.company.domain.utils.retries

import com.company.domain.executor.ExecutionScheduler


class FlowableRetryExponentialDefault(
        override val executionScheduler: ExecutionScheduler
) : FlowableRetryExponential(
        DEFAULT_MAX_RETRIES,
        executionScheduler
)