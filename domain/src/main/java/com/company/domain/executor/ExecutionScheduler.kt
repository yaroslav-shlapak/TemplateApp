package com.company.domain.executor

import io.reactivex.*

interface ExecutionScheduler {
    fun ui(): Scheduler
    fun highPriority(): Scheduler
    fun lowPriority(): Scheduler
    fun io(): Scheduler
    fun computation(): Scheduler
    fun singleThread(): Scheduler

    fun <T> highPrioritySingle(): (Single<T>) -> Single<T>
    fun <T> lowPrioritySingle(): (Single<T>) -> Single<T>
    fun <T> ioSingle(): (Single<T>) -> Single<T>
    fun <T> computationSingle(): (Single<T>) -> Single<T>

    fun <T> highPriorityObservable(): (Observable<T>) -> Observable<T>
    fun <T> lowPriorityObservable(): (Observable<T>) -> Observable<T>
    fun <T> ioObservable(): (Observable<T>) -> Observable<T>
    fun <T> computationObservable(): (Observable<T>) -> Observable<T>

    fun <T> highPriorityFlowable(): (Flowable<T>) -> Flowable<T>
    fun <T> lowPriorityFlowable(): (Flowable<T>) -> Flowable<T>
    fun <T> ioFlowable(): (Flowable<T>) -> Flowable<T>
    fun <T> computationFlowable(): (Flowable<T>) -> Flowable<T>

    fun highPriorityCompletable(): (Completable) -> Completable
    fun lowPriorityCompletable(): (Completable) -> Completable
    fun ioCompletable(): (Completable) -> Completable
    fun computationCompletable(): (Completable) -> Completable
}
