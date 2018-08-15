package com.abeemukthees.domain.usecases

import com.abeemukthees.domain.action.TaskToggledAction
import com.abeemukthees.domain.action.ToggleTodoAction
import com.abeemukthees.domain.executor.PostExecutionThread
import com.abeemukthees.domain.executor.ThreadExecutor
import com.abeemukthees.domain.interactor.ObservableUseCase
import com.abeemukthees.domain.repository.Repository
import com.abeemukthees.domain.statemachine.Action
import com.abeemukthees.domain.statemachine.State
import com.freeletics.rxredux.StateAccessor
import io.reactivex.Observable

class ToggleTask(private val repository: Repository,
                 threadExecutor: ThreadExecutor,
                 postExecutionThread: PostExecutionThread) : ObservableUseCase(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(action: Observable<Action>, state: StateAccessor<State>): Observable<Action> {
        return action.ofType(ToggleTodoAction::class.java)
                .switchMap { repository.toogleTask(it.id).toObservable<TaskToggledAction>() }
    }
}