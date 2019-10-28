package com.maruiz.arrowexample.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.None
import arrow.fx.extensions.io.unsafeRun.runNonBlocking
import arrow.unsafe
import com.maruiz.arrowexample.data.model.BookModel
import com.maruiz.arrowexample.domain.GetBooks
import com.maruiz.arrowexample.presentation.presentationmodel.BookPresentationModel
import com.maruiz.arrowexample.presentation.utils.Event

class BooksViewModel(private val getBooks: GetBooks) : BaseViewModel() {
    private val books = MutableLiveData<List<BookPresentationModel>>()
    fun observeBooks(): LiveData<List<BookPresentationModel>> = books

    private val navigateToDetail = MutableLiveData<Event<BookPresentationModel>>()
    fun navigateToDetail(): LiveData<Event<BookPresentationModel>> = navigateToDetail

    fun requestBooks() = unsafe {
        runNonBlocking({ getBooks(None, viewModelScope.coroutineContext) }) {
            it.fold(::handleFailure, ::handleSuccess)
        }
    }

    fun bookSelected(book: BookPresentationModel) {
        navigateToDetail.value = Event(book)
    }

    private fun handleSuccess(books: List<BookModel>) {
        this.books.value =
            books.map {
                BookPresentationModel(
                    it.title,
                    it.author,
                    it.shortSynopsis,
                    it.synopsis,
                    it.image
                )
            }
    }
}