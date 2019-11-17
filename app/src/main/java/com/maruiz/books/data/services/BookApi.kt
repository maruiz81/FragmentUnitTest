package com.maruiz.books.data.services

import arrow.integrations.retrofit.adapter.CallK
import com.maruiz.books.data.model.BookModel
import retrofit2.http.GET

interface BookApi {
    @GET("BookList.json")
    fun getBooks(): CallK<List<BookModel>>
}