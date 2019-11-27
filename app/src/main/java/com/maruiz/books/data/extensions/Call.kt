package com.maruiz.books.data.extensions

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import com.maruiz.books.data.error.Failure
import retrofit2.Call

fun <R> Call<R>.makeCall(default: R): Either<Failure, R> =
    this.execute().run {
        if (isSuccessful) Right(body() ?: default)
        else Left(Failure.GenericError)
    }