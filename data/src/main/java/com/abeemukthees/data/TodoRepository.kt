package com.abeemukthees.data

import com.abeemukthees.domain.entities.Task
import com.abeemukthees.domain.repository.Repository
import io.reactivex.Completable
import io.reactivex.Observable

class TodoRepository(private val dataStoreFactory: DataStoreFactory) : Repository {

    override fun getTasks(): Observable<List<Task>> {
        return dataStoreFactory.dummyDataStore.getTasks()
    }

    override fun addTask(text: String): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toogleTask(id: Int): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}