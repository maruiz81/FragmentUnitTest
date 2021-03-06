package com.maruiz.books.presentation.presentationmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookPresentationModel(
    val title: String = "", val author: String = "", val shortSynopsis: String = "",
    val synopsis: String = "", val image: String = ""
) : Parcelable