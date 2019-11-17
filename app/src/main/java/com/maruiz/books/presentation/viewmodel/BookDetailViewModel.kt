package com.maruiz.books.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maruiz.books.presentation.presentationmodel.BookDetailPresentationModel
import com.maruiz.books.presentation.presentationmodel.BookPresentationModel

class BookDetailViewModel : BaseViewModel() {
    private val book = MutableLiveData<BookDetailPresentationModel>()
    fun observeBook(): LiveData<BookDetailPresentationModel> = book

    fun showBook(bookReceived: BookPresentationModel) {
        book.value = BookDetailPresentationModel(
            title = bookReceived.title,
            author = bookReceived.author,
            image = bookReceived.image,
            synopsis = bookReceived.synopsis
        )
    }
}