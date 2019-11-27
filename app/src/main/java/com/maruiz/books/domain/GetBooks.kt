package com.maruiz.books.domain

import arrow.core.Either
import arrow.core.None
import com.maruiz.books.data.error.Failure
import com.maruiz.books.data.model.BookModel
import com.maruiz.books.data.repository.BookRepository

class GetBooks(private val bookRepository: BookRepository) : UseCase<List<BookModel>, None>() {
    override suspend fun run(params: None): Either<Failure, List<BookModel>> =
        bookRepository.getBooks()
}
