package com.company.app.core.executor

import com.company.domain.executor.ExecutionScheduler
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ThreadScheduler : ExecutionScheduler {
    companion object {
        private const val HIGH_PRIORITY_THREADS = 8
        private const val LOW_PRIORITY_THREADS = 4

        internal val highPriorityScheduler: Scheduler
        internal val lowPriorityScheduler: Scheduler
        internal val ioScheduler: Scheduler
        internal val computationScheduler: Scheduler
        internal val singleThread: Scheduler

        init {
            highPriorityScheduler = Schedulers.from(
                    JobExecutor(
                            HIGH_PRIORITY_THREADS,
                            "High-Priority-Pool"
                    )
            )
            lowPriorityScheduler = Schedulers.from(
                    JobExecutor(
                            LOW_PRIORITY_THREADS,
                            "Low-Priority-Pool"
                    )
            )
            ioScheduler = Schedulers.io()
            computationScheduler = Schedulers.computation()
            singleThread = Schedulers.from(
                    JobExecutor(
                            1,
                            "Single-Threaded-Pool"
                    )
            )
        }
    }

    override fun ui(): Scheduler = AndroidSchedulers.mainThread()
    override fun highPriority(): Scheduler = highPriorityScheduler
    override fun lowPriority(): Scheduler = lowPriorityScheduler
    override fun io(): Scheduler = ioScheduler
    override fun computation(): Scheduler = computationScheduler
    override fun singleThread(): Scheduler = singleThread

    override fun <T> highPrioritySingle() =
            { upstream: Single<T> -> upstream.subscribeOn(highPriority()) }

    override fun <T> lowPrioritySingle() =
            { upstream: Single<T> -> upstream.subscribeOn(lowPriority()) }

    override fun <T> ioSingle() =
            { upstream: Single<T> -> upstream.subscribeOn(io()) }

    override fun <T> computationSingle() =
            { upstream: Single<T> -> upstream.subscribeOn(computation()) }

    override fun <T> highPriorityObservable() =
            { upstream: Observable<T> -> upstream.subscribeOn(highPriority()) }

    override fun <T> lowPriorityObservable() =
            { upstream: Observable<T> -> upstream.subscribeOn(lowPriority()) }

    override fun <T> ioObservable() =
            { upstream: Observable<T> -> upstream.subscribeOn(io()) }

    override fun <T> computationObservable() =
            { upstream: Observable<T> -> upstream.subscribeOn(computation()) }

    override fun <T> highPriorityFlowable() =
            { upstream: Flowable<T> -> upstream.subscribeOn(highPriority()) }

    override fun <T> lowPriorityFlowable() =
            { upstream: Flowable<T> -> upstream.subscribeOn(lowPriority()) }

    override fun <T> ioFlowable() =
            { upstream: Flowable<T> -> upstream.subscribeOn(io()) }

    override fun <T> computationFlowable() =
            { upstream: Flowable<T> -> upstream.subscribeOn(computation()) }

    override fun highPriorityCompletable() =
            { upstream: Completable -> upstream.subscribeOn(highPriority()) }

    override fun lowPriorityCompletable() =
            { upstream: Completable -> upstream.subscribeOn(lowPriority()) }

    override fun ioCompletable() =
            { upstream: Completable -> upstream.subscribeOn(io()) }

    override fun computationCompletable() =
            { upstream: Completable -> upstream.subscribeOn(computation()) }
}
