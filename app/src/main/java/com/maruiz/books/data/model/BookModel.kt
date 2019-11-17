package com.maruiz.books.data.model

import com.squareup.moshi.Json

data class BookModel(
    val bookId: Int = 0, val title: String = "", val author: String = "",
    @Json(name = "short_synopsis") val shortSynopsis: String = "", val synopsis: String = "",
    val image: String = ""
)