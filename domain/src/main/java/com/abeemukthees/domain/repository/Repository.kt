package com.abeemukthees.domain.repository

import com.abeemukthees.domain.entities.Task
import io.reactivex.Completable
import io.reactivex.Observable

interface Repository {

    fun getTasks(): Observable<List<Task>>

    fun addTask(text: String): Completable

    fun toogleTask(id: Int): Completable
}