package com.company.app.di.modules

import com.company.app.core.executor.ThreadScheduler
import com.company.domain.executor.ExecutionScheduler
import com.company.domain.utils.retries.FlowableRetry
import com.company.domain.utils.retries.FlowableRetryExponentialDefault
import com.company.domain.utils.retrywithtimeout.DefaultRetryWithTimeout
import com.company.domain.utils.retrywithtimeout.RetryWithTimeout
import com.company.domain.utils.timeout.DefaultTimeout
import com.company.domain.utils.timeout.Timeout
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created on 2019/02/25.
 */
@Module
class ThreadingModule {

    @Singleton
    @Provides
    internal fun providesExecutionScheduler(): ExecutionScheduler {
        return ThreadScheduler()
    }

    @Provides
    internal fun provideTimeout(
            executionScheduler: ExecutionScheduler
    ): Timeout {
        return DefaultTimeout(executionScheduler = executionScheduler)
    }

    @Provides
    internal fun provideRetryrWithTimeout(
        flowableRetry: FlowableRetry,
        timeout: Timeout
    ): RetryWithTimeout {
        return DefaultRetryWithTimeout(
                flowableRetry,
                timeout
        )
    }

    @Provides
    internal fun provideFlowableRetry(
            executionScheduler: ExecutionScheduler
    ): FlowableRetry {
        return FlowableRetryExponentialDefault(executionScheduler)
    }
}