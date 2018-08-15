package com.abeemukthees.domain.statemachine

import com.abeemukthees.domain.action.*
import com.abeemukthees.domain.common.VisibilityMode
import com.abeemukthees.domain.executor.PostExecutionThread
import com.abeemukthees.domain.executor.ThreadExecutor
import com.abeemukthees.domain.repository.Repository
import com.abeemukthees.domain.state.*
import com.abeemukthees.domain.usecases.AddTask
import com.abeemukthees.domain.usecases.GetTasks
import com.abeemukthees.domain.usecases.ToggleTask
import com.freeletics.rxredux.StateAccessor
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable

class AppStateMachine(private val repository: Repository,
                      private val threadExecutor: ThreadExecutor,
                      private val postExecutionThread: PostExecutionThread) {

    val input: Relay<Action> = PublishRelay.create()

    val state: Observable<State> = input
            .doOnNext { println("Input Action $it") }
            .reduxStore(
                    initialState = LoadingTasksState,
                    sideEffects = listOf(::loadTasks, ::addTask, ::toggleTask),
                    reducer = ::reducer)
            .distinctUntilChanged()
            .doOnNext { println("RxStore state $it") }


    private fun loadTasks(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> {
        return GetTasks(repository, threadExecutor, postExecutionThread).execute(observer = null, action = actions, state = state)
    }

    private fun addTask(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> {
        return actions.ofType(AddTodoAction::class.java).switchMap {
            AddTask(repository, threadExecutor, postExecutionThread).execute(observer = null, action = actions, state = state)
        }
    }

    private fun toggleTask(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> {
        return actions.ofType(ToggleTodoAction::class.java).switchMap {
            ToggleTask(repository, threadExecutor, postExecutionThread).execute(observer = null, action = actions, state = state)
        }
    }

    private fun reducer(state: State, action: Action): State {
        return when (action) {
            is LoadTasksAction -> LoadingTasksState
            is SetVisibilityFilterAction -> FilteringTasksState(action.visibilityMode)
            is TasksLoadedAction -> {
                when (action.visibilityMode) {

                    VisibilityMode.ALL -> ShowAllTasksState(action.tasks)
                    VisibilityMode.ACTIVE -> ShowActiveTasksState(action.tasks)
                    VisibilityMode.COMPLETED -> ShowCompletedTasksState(action.tasks)
                }
            }
            is AddTodoAction -> state
            else -> state

        }
    }
}