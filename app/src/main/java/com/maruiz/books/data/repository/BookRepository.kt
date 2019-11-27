package com.maruiz.books.data.repository

import arrow.core.Either
import com.maruiz.books.data.error.Failure
import com.maruiz.books.data.extensions.makeCall
import com.maruiz.books.data.model.BookModel
import com.maruiz.books.data.services.BookApi

interface BookRepository {
    fun getBooks(): Either<Failure, List<BookModel>>

    class Network(private val bookService: BookApi) : BookRepository {
        override fun getBooks(): Either<Failure, List<BookModel>> =
            bookService.getBooks().makeCall(emptyList())
    }
}