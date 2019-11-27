package com.maruiz.books.data.services

import com.maruiz.books.data.model.BookModel
import retrofit2.Call
import retrofit2.http.GET

interface BookApi {
    @GET("BookList.json")
    fun getBooks(): Call<List<BookModel>>
}