package com.maruiz.books.domain.di

import com.maruiz.books.domain.usecases.GetBooks
import org.koin.dsl.module

val domainModule = module {
    factory { GetBooks(get()) }
}