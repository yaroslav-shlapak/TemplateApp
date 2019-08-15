package com.company.domain.utils.timeout

import io.reactivex.Flowable
import io.reactivex.Scheduler
import java.util.concurrent.TimeUnit

/**
 * Created on 2019/01/30.
 */
interface Timeout {
    fun timeoutTime(): Long
    fun timeoutTimeUnit(): TimeUnit
    fun scheduler(): Scheduler
    fun <T> flowableTimeout(): (Flowable<T>) -> Flowable<T>
}