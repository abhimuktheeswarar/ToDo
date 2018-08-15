package com.abeemukthees.todo.di

import com.abeemukthees.data.DataStoreFactory
import com.abeemukthees.data.TodoRepository
import com.abeemukthees.data.executor.JobExecutor
import com.abeemukthees.domain.executor.PostExecutionThread
import com.abeemukthees.domain.executor.ThreadExecutor
import com.abeemukthees.domain.repository.Repository
import com.abeemukthees.domain.statemachine.AppStateMachine
import com.abeemukthees.todo.TodoViewModel
import com.abeemukthees.todo.UiThread
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

val TodoAppModule: Module = applicationContext {

    bean { DataStoreFactory(get()) }
    bean { TodoRepository(get()) as Repository }
    bean { JobExecutor() as ThreadExecutor }
    bean { UiThread() as PostExecutionThread }
    bean { AppStateMachine(get(), get(), get()) }
}


val viewModelModule = applicationContext {

    viewModel { TodoViewModel(get()) }

}
