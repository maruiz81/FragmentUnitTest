package com.maruiz.books.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maruiz.books.data.error.Failure

abstract class BaseViewModel : ViewModel() {

    private val failure = MutableLiveData<Failure>()

    fun observeFailure(): LiveData<Failure> = failure

    protected fun handleFailure(failure: Failure) {
        this.failure.value = failure
    }
}