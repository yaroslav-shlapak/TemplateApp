package com.company.domain.usecase

import com.company.domain.usecase.UseCaseTest.assertDisposablesAreDisposed
import io.kotlintest.shouldBe
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import org.spekframework.spek2.Spek
import java.util.concurrent.TimeUnit

/** tests for
 * @see com.company.domain.usecase.UseCase
 **/
object UseCaseTest : Spek({
    val rxSingleUseCase: TestRxSingleUseCase by memoized { TestRxSingleUseCase() }
    val rxObservableUseCase: TestRxObservableUseCase by memoized { TestRxObservableUseCase() }
    val rxFlowableUseCase: TestRxFlowableUseCase by memoized { TestRxFlowableUseCase() }
    val rxCompletableUseCase: TestUseCaseRxCompletable by memoized { TestUseCaseRxCompletable() }

    /** tests for
     * @see com.company.domain.usecase.UseCase.RxSingle
     **/
    group("Single") {
        test("should dispose disposables in RxSingle UseCase using Observer") {
            rxSingleUseCase.execute(TestUseCaseRxSingleObserver(), UseCase.None)
            assertDisposablesAreDisposed(rxSingleUseCase)
        }
        test("should dispose disposables in RxSingle UseCase using Functions") {
            rxSingleUseCase.execute({ }, { }, UseCase.None)
            assertDisposablesAreDisposed(rxSingleUseCase)
        }
    }

    /** tests for
     * @see com.company.domain.usecase.UseCase.RxObservable
     **/
    group("Observable") {
        test("should dispose disposables in RxObservable UseCase using Observer") {
            rxObservableUseCase.execute(TestUseCaseRxObservableObserver(), UseCase.None)
            assertDisposablesAreDisposed(rxObservableUseCase)
        }
        test("should dispose disposables in RxObservable UseCase using Functions") {
            rxObservableUseCase.execute({ }, { }, UseCase.None)
            assertDisposablesAreDisposed(rxObservableUseCase)
        }
    }

    /** tests for
     * @see com.company.domain.usecase.UseCase.RxFlowable
     **/
    group("Flowable") {
        test("should dispose disposables in RxFlowable UseCase using Observer") {
            rxFlowableUseCase.execute(TestUseCaseRxFlowableObserver(), UseCase.None)
            assertDisposablesAreDisposed(rxFlowableUseCase)
        }
        test("should dispose disposables in RxFlowable UseCase using Functions") {
            rxFlowableUseCase.execute({ }, { }, UseCase.None)
            assertDisposablesAreDisposed(rxFlowableUseCase)
        }
    }

    /** tests for
     * @see com.company.domain.usecase.UseCase.RxCompletable
     **/
    group("Completable") {
        test("should dispose disposables in RxCompletable UseCase with Empty Params") {
            rxCompletableUseCase.execute(UseCase.None)
            assertDisposablesAreDisposed(rxCompletableUseCase)
        }
        test("should dispose disposables in RxCompletable UseCase using Functions") {
            rxCompletableUseCase.execute({ }, UseCase.None)
            assertDisposablesAreDisposed(rxCompletableUseCase)
        }
    }

    /** tests for
     * @see com.company.domain.usecase.UseCase.clear
     **/
    group("clear") {
        test("should clear disposables") {
            val useCase = object : UseCase<Single<String>, UseCase.None>() {
                override fun build(params: None): Single<String> =
                        Single.fromCallable { "test" }.delay(1000, TimeUnit.MILLISECONDS)
            }
            useCase.disposables.add(useCase.build(UseCase.None).subscribe())
            useCase.disposables.isDisposed shouldBe false
            useCase.disposables.size() shouldBe 1
            useCase.clear()
            useCase.disposables.isDisposed shouldBe false
            useCase.disposables.size() shouldBe 0

        }
    }
}) {

    fun assertDisposablesAreDisposed(useCase: UseCase<Any, UseCase.None>) {
        useCase.disposables.isDisposed shouldBe false
        useCase.dispose()
        useCase.disposables.isDisposed shouldBe true
    }

    private class TestUseCaseRxSingleObserver : UseCaseObserver.RxSingle<String>()
    private class TestRxSingleUseCase : UseCase.RxSingle<String, UseCase.None>() {
        override fun build(params: None): Single<String> = Single.just("test")
    }

    private class TestUseCaseRxObservableObserver : UseCaseObserver.RxObservable<String>()
    private class TestRxObservableUseCase : UseCase.RxObservable<String, UseCase.None>() {
        override fun build(params: None): Observable<String> = Observable.just("test")
    }

    private class TestUseCaseRxFlowableObserver : UseCaseObserver.RxFlowable<String>()
    private class TestRxFlowableUseCase : UseCase.RxFlowable<String, UseCase.None>() {
        override fun build(params: None): Flowable<String> = Flowable.just("test")
    }

    private class TestUseCaseRxCompletable : UseCase.RxCompletable<UseCase.None>() {
        override fun build(params: None): Completable = Completable.complete()
    }
}
