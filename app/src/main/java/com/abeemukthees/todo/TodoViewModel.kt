package com.abeemukthees.todo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.abeemukthees.domain.statemachine.Action
import com.abeemukthees.domain.statemachine.AppStateMachine
import com.abeemukthees.domain.statemachine.State
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer

class TodoViewModel(appStateMachine: AppStateMachine) : ViewModel() {

    private val inputRelay: Relay<Action> = PublishRelay.create()
    private val mutableState = MutableLiveData<State>()
    private val disposables = CompositeDisposable()

    val input: Consumer<Action> = inputRelay
    val state: LiveData<State> = mutableState

    init {
        disposables.add(inputRelay.subscribe(appStateMachine.input))
        disposables.add(
                appStateMachine.state
                        .subscribe { state -> mutableState.value = state }
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}