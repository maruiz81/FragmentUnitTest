package com.maruiz.books.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.fx.IO
import com.maruiz.books.data.model.BookModel
import com.maruiz.books.domain.GetBooks
import com.maruiz.books.presentation.di.appModule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.Times
import java.lang.Exception

@RunWith(JUnit4::class)
class BooksViewModelTest : AutoCloseKoinTest() {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getBooks: GetBooks

    private val booksViewModel: BooksViewModel by inject()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        startKoin {
            modules(
                listOf(
                    appModule,
                    module(override = true) {
                        factory { getBooks }
                    }
                ))
        }
    }

    @Test
    fun `verify useCase executed`() {
        whenever(getBooks.invoke(any(), any())).thenReturn(IO.just(emptyList()))
        booksViewModel.requestBooks()

        verify(getBooks, Times(1)).invoke(any(), any())
    }

    @Test
    fun `get properly list of ten Books`() {
        val itemsNumber = 10
        val bookList = getBookList(itemsNumber)
        whenever(getBooks.invoke(any(), any())).thenReturn(
            IO.just(bookList)
        )
        booksViewModel.requestBooks()

        booksViewModel.observeBooks().value!!.run {
            size shouldEqual itemsNumber
            forEachIndexed { index, book ->
                book.title shouldEqual bookList[index].title
                book.author shouldEqual bookList[index].author
                book.shortSynopsis shouldEqual bookList[index].shortSynopsis
                book.synopsis shouldEqual bookList[index].synopsis
                book.image shouldEqual bookList[index].image
            }
        }
    }

    @Test
    fun `should show error when getting error from useCase`() {
        whenever(getBooks.invoke(any(), any())).thenThrow(Exception())

        booksViewModel.requestBooks()

        booksViewModel.observeFailure().value!!.run {

        }
    }

    private fun getBookList(size: Int): List<BookModel> =
        (1..size).map {
            BookModel(
                it, "Book $it", "Author $it", "Short Synopsis $it",
                "Synopsis $it", "image url $it"
            )
        }
}