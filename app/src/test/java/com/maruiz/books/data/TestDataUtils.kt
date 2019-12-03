package com.maruiz.books.data

import com.maruiz.books.data.model.BookModelDataModel
import com.maruiz.books.domain.model.BookModelDomainModel

fun getBookListData(size: Int): List<BookModelDataModel> =
    (1..size).map {
        BookModelDataModel(
            it, "Book $it", "Author $it", "Short Synopsis $it",
            "Synopsis $it", "image url $it"
        )
    }

fun getBookListDomain(size: Int): List<BookModelDomainModel> =
    (1..size).map {
        BookModelDomainModel(
            "Book $it", "Author $it", "Short Synopsis $it",
            "Synopsis $it", "image url $it"
        )
    }