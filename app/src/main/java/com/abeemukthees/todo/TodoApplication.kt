package com.abeemukthees.todo

import android.app.Application
import com.abeemukthees.todo.di.TodoAppModule
import com.abeemukthees.todo.di.viewModelModule
import org.koin.android.ext.android.startKoin

class TodoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(TodoAppModule, viewModelModule))

    }


}