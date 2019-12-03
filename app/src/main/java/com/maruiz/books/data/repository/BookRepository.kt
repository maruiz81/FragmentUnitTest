package com.maruiz.books.data.repository

import arrow.core.Either
import com.maruiz.books.data.error.Failure
import com.maruiz.books.data.extensions.makeCall
import com.maruiz.books.data.model.BookModelDataModel
import com.maruiz.books.data.services.BookApi

interface BookRepository {
    suspend fun getBooks(): Either<Failure, List<BookModelDataModel>>

    class Network(private val bookService: BookApi) : BookRepository {
        override suspend fun getBooks(): Either<Failure, List<BookModelDataModel>> =
            bookService.getBooks().makeCall(emptyList())
    }
}