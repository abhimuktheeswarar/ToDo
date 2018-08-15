package com.abeemukthees.domain.interactor

import com.abeemukthees.domain.executor.PostExecutionThread
import com.abeemukthees.domain.executor.ThreadExecutor
import com.abeemukthees.domain.statemachine.Action
import com.abeemukthees.domain.statemachine.State
import com.freeletics.rxredux.StateAccessor
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers


/**
 * Abstract class for a UseCase that returns an instance of a [Observable].
 */
abstract class ObservableUseCase constructor(
        private val threadExecutor: ThreadExecutor,
        private val postExecutionThread: PostExecutionThread) {

    private val disposables = CompositeDisposable()

    /**
     * Builds a [Observable] which will be used when the current [ObservableUseCase] is executed.
     */
    protected abstract fun buildUseCaseObservable(action: Observable<Action>, state: StateAccessor<State>): Observable<Action>

    /**
     * Executes the current use case.
     */
    open fun execute(observer: DisposableObserver<Action>?, action: Observable<Action>, state: StateAccessor<State>) : Observable<Action> {
        val observable = this.buildUseCaseObservable(action, state)
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.scheduler) as Observable<Action>
        observer?.let { addDisposable(observable.subscribeWith(it)) }
        return observable
    }

    /**
     * Dispose from current [CompositeDisposable].
     */
    fun dispose() {
        if (!disposables.isDisposed) disposables.dispose()
    }

    /**
     * Dispose from current [CompositeDisposable].
     */
    private fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

}