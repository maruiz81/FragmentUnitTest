package com.maruiz.books.presentation.di

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import arrow.integrations.retrofit.adapter.CallKindAdapterFactory
import coil.ImageLoaderBuilder
import com.maruiz.books.data.services.BookApi
import com.maruiz.books.domain.GetBooks
import com.maruiz.books.presentation.adapter.BooksAdapter
import com.maruiz.books.presentation.viewmodel.BookDetailViewModel
import com.maruiz.books.presentation.viewmodel.BooksViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.android.ext.koin.androidContext
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
    factory { GetBooks(get()) }
    factory { BooksAdapter(get()) }
    viewModel { BooksViewModel(get()) }
    viewModel { BookDetailViewModel() }
    single { ImageLoaderBuilder(androidContext()).build() }
    factory { (fragment: Fragment) ->
        fragment.findNavController()
    }
}