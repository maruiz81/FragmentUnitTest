package com.maruiz.books.domain

import arrow.core.None
import arrow.fx.IO
import com.maruiz.books.data.extensions.makeCall
import com.maruiz.books.data.model.BookModel
import com.maruiz.books.data.services.BookApi

class GetBooks(private val bookApi: BookApi) : UseCase<List<BookModel>, None>() {
    override fun run(params: None): IO<List<BookModel>> = bookApi.getBooks().makeCall(emptyList())
}
