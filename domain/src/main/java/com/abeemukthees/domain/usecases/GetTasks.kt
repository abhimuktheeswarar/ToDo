package com.abeemukthees.domain.usecases

import com.abeemukthees.domain.action.LoadTasksAction
import com.abeemukthees.domain.action.SetVisibilityFilterAction
import com.abeemukthees.domain.action.TasksLoadedAction
import com.abeemukthees.domain.common.VisibilityMode
import com.abeemukthees.domain.executor.PostExecutionThread
import com.abeemukthees.domain.executor.ThreadExecutor
import com.abeemukthees.domain.interactor.ObservableUseCase
import com.abeemukthees.domain.repository.Repository
import com.abeemukthees.domain.state.FilteringTasksState
import com.abeemukthees.domain.statemachine.Action
import com.abeemukthees.domain.statemachine.State
import com.freeletics.rxredux.StateAccessor
import io.reactivex.Observable

class GetTasks(private val repository: Repository,
               threadExecutor: ThreadExecutor,
               postExecutionThread: PostExecutionThread) : ObservableUseCase(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(action: Observable<Action>, state: StateAccessor<State>): Observable<Action> {
        return action.filter { it is LoadTasksAction || it is SetVisibilityFilterAction }
                .switchMap { repository.getTasks() }
                .map { tasks ->

                    if (state() is FilteringTasksState) {
                        val visibilityMode = (state() as FilteringTasksState).visibilityMode
                        println("Filtering for $visibilityMode")

                        when (visibilityMode) {

                            VisibilityMode.ALL -> TasksLoadedAction(tasks, visibilityMode)
                            VisibilityMode.ACTIVE -> TasksLoadedAction(tasks.filter { !it.isCompleted }, visibilityMode)
                            VisibilityMode.COMPLETED -> TasksLoadedAction(tasks.filter { it.isCompleted }, visibilityMode)
                        }
                    } else {
                        println("Not done any filtering")
                        TasksLoadedAction(tasks)
                    }
                }
    }
}