package com.maruiz.books.data.error

sealed class Failure {
    object GenericError : Failure()
}