package com.maruiz.arrowexample.presentation.di

import arrow.integrations.retrofit.adapter.CallKindAdapterFactory
import com.maruiz.arrowexample.data.services.BookApi
import com.maruiz.arrowexample.domain.GetBooks
import com.maruiz.arrowexample.presentation.adapter.BooksAdapter
import com.maruiz.arrowexample.presentation.viewmodel.BooksViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(getProperty<String>("base_url"))
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .addCallAdapterFactory(CallKindAdapterFactory.create())
            .build()
    }
    single { get<Retrofit>().create(BookApi::class.java) }
    single { GetBooks(get()) }
    factory { BooksAdapter() }
    viewModel { BooksViewModel(get()) }
}