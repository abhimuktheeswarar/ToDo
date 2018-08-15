package com.abeemukthees.data.dummy

import android.content.Context
import com.abeemukthees.domain.entities.Task
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class DummyDataStore(private val context: Context) {

    val tasks = LinkedHashMap<Int, Task>()

    init {
        for (i in 0..10) {

            tasks[i] = Task(i, "SampleTask$i", i % 2 == 0)
        }
    }

    fun getTasks(): Observable<List<Task>> {
        return Observable.just(tasks.values.toList()).delay(2, TimeUnit.SECONDS)
    }

    fun addTask(text: String): Completable {
        return Completable.complete()
    }

    fun toogleTask(id: Int): Completable {
        return Completable.complete()
    }
}