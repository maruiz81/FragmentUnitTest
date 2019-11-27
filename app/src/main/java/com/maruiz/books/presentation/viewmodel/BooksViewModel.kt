package com.maruiz.books.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.None
import com.maruiz.books.data.model.BookModel
import com.maruiz.books.domain.GetBooks
import com.maruiz.books.presentation.presentationmodel.BookPresentationModel
import com.maruiz.books.presentation.utils.Event

class BooksViewModel(private val getBooks: GetBooks) : BaseViewModel() {

    private val books = MutableLiveData<List<BookPresentationModel>>()
    fun observeBooks(): LiveData<List<BookPresentationModel>> = books

    private val navigateToDetail = MutableLiveData<Event<BookPresentationModel>>()
    fun navigateToDetail(): LiveData<Event<BookPresentationModel>> = navigateToDetail

    fun requestBooks() =
        getBooks(None, viewModelScope) { it.fold(::handleFailure, ::handleSuccess) }

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