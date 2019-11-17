package com.maruiz.books.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maruiz.books.domain.UseCase

abstract class BaseViewModel(private vararg val usecases: UseCase<*, *>) : ViewModel() {

    private val failure = MutableLiveData<String>()

    fun observeFailure(): LiveData<String> = failure

    protected fun handleFailure(failure: Throwable) {
        this.failure.value = failure.message ?: ""
    }

    override fun onCleared() {
        super.onCleared()

        usecases.forEach { it.cancel() }
    }
}