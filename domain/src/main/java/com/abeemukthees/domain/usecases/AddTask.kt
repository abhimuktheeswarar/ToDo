package com.abeemukthees.domain.usecases

import com.abeemukthees.domain.action.AddTodoAction
import com.abeemukthees.domain.action.TaskAddedAction
import com.abeemukthees.domain.executor.PostExecutionThread
import com.abeemukthees.domain.executor.ThreadExecutor
import com.abeemukthees.domain.interactor.ObservableUseCase
import com.abeemukthees.domain.repository.Repository
import com.abeemukthees.domain.statemachine.Action
import com.abeemukthees.domain.statemachine.State
import com.freeletics.rxredux.StateAccessor
import io.reactivex.Observable

class AddTask(private val repository: Repository,
              threadExecutor: ThreadExecutor,
              postExecutionThread: PostExecutionThread) : ObservableUseCase(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(action: Observable<Action>, state: StateAccessor<State>): Observable<Action> {
        return action.ofType(AddTodoAction::class.java)
                .switchMap { repository.addTask((it.text)).toObservable<TaskAddedAction>() }
    }
}