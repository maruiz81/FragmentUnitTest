package com.maruiz.arrowexample.data.services

import arrow.integrations.retrofit.adapter.CallK
import com.maruiz.arrowexample.data.model.BookModel
import retrofit2.http.GET

interface BookApi {
    @GET("BookList.json")
    fun getBooks(): CallK<List<BookModel>>
}