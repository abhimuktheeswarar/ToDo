package com.abeemukthees.domain.state

import com.abeemukthees.domain.common.VisibilityMode
import com.abeemukthees.domain.entities.Task
import com.abeemukthees.domain.statemachine.State

object LoadingTasksState : State

data class FilteringTasksState(val visibilityMode: VisibilityMode) : State

data class ShowAllTasksState(val tasks: List<Task>) : State

data class ShowActiveTasksState(val tasks: List<Task>) : State

data class ShowCompletedTasksState(val tasks: List<Task>) : State

data class ShowFilteredTasksState(val tasks: List<Task>) : State