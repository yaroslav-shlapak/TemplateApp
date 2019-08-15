package com.company.domain.usecase

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

abstract class UseCase<out Type, in Params> where Type : Any {

    val disposables = CompositeDisposable()

    fun dispose() = disposables.dispose()

    fun clear() = disposables.clear()

    abstract fun build(params: Params): Type

    abstract class RxSingle<T, in P> : UseCase<Single<T>, P>() {
        fun execute(observer: UseCaseObserver.RxSingle<T>, params: P) =
                disposables.add(build(params).subscribeWith(observer))

        fun execute(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit, params: P) =
                disposables.add(build(params).subscribe(onSuccess, onError))
    }

    abstract class RxObservable<T, in P> : UseCase<Observable<T>, P>() {
        fun execute(observer: UseCaseObserver.RxObservable<T>, params: P) =
                disposables.add(build(params).subscribeWith(observer))

        fun execute(onNext: (T) -> Unit, onError: (Throwable) -> Unit, params: P) {
            disposables.add(build(params).subscribe(onNext, onError))
        }
    }

    abstract class RxFlowable<T, in P> : UseCase<Flowable<T>, P>() {
        fun execute(subscriber: UseCaseObserver.RxFlowable<T>, params: P) =
                disposables.add(build(params).subscribeWith(subscriber))

        fun execute(onNext: (T) -> Unit, onError: (Throwable) -> Unit, params: P) {
            disposables.add(build(params).subscribe(onNext, onError))
        }
    }

    abstract class RxCompletable<in P> : UseCase<Completable, P>() {
        fun execute(params: P) = execute({}, params)

        fun execute(onComplete: () -> Unit = {}, params: P): Boolean {
            return disposables.add(build(params).subscribe(onComplete))
        }
    }

    object None
}
