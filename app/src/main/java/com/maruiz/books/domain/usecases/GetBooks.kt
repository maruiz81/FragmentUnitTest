package com.maruiz.books.domain.usecases

import arrow.core.Either
import com.maruiz.books.data.error.Failure
import com.maruiz.books.data.repository.BookRepository
import com.maruiz.books.domain.model.BookModelDomainModel

class GetBooks(private val bookRepository: BookRepository) :
    UseCase<List<BookModelDomainModel>, Unit>() {
    override suspend fun run(params: Unit): Either<Failure, List<BookModelDomainModel>> =
        bookRepository.getBooks().map { success ->
            success.map {
                BookModelDomainModel(
                    it.title,
                    it.author,
                    it.shortSynopsis,
                    it.synopsis,
                    it.image
                )
            }
        }
}
