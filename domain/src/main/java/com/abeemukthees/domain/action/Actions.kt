package com.abeemukthees.domain.action

import com.abeemukthees.domain.common.VisibilityMode
import com.abeemukthees.domain.entities.Task
import com.abeemukthees.domain.statemachine.Action


//User Actions
data class AddTodoAction(val text: String) : Action

data class SetVisibilityFilterAction(val visibilityMode: VisibilityMode) : Action

data class ToggleTodoAction(val id: Int) : Action

//Internal Actions
object LoadTasksAction : Action

data class TasksLoadedAction(val tasks: List<Task>, val visibilityMode: VisibilityMode = VisibilityMode.ALL) : Action

object TaskAddedAction : Action

object TaskToggledAction : Action




