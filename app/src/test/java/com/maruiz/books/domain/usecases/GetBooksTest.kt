package com.maruiz.books.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Left
import arrow.core.Right
import com.maruiz.books.data.di.dataModule
import com.maruiz.books.data.error.Failure
import com.maruiz.books.data.getBookListData
import com.maruiz.books.data.repository.BookRepository
import com.maruiz.books.domain.di.domainModule
import com.maruiz.books.presentation.di.presentationModule
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeInstanceOf
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

@RunWith(JUnit4::class)
class GetBooksTest : AutoCloseKoinTest() {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var bookRepository: BookRepository

    private val getBooks: GetBooks by inject()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        startKoin {
            modules(
                listOf(
                    dataModule,
                    domainModule,
                    module(override = true) {
                        single { bookRepository }
                    }
                ))
        }
    }

    @Test
    fun `check return empty when repository return empty`() {
        runBlocking {
            whenever(bookRepository.getBooks()).thenReturn(Right(emptyList()))
            getBooks.run(Unit).fold({ assert(false) }, {
                it.size shouldEqual 0
            })

            verify(bookRepository, Times(1)).getBooks()
        }
    }

    @Test
    fun `check return domain items from the data originals items`() {
        runBlocking {
            val numerOfItems = 10
            val dataItems = getBookListData(10)
            whenever(bookRepository.getBooks()).thenReturn(Right(dataItems))
            getBooks.run(Unit).fold({ assert(false) }, { bookList ->
                bookList.size shouldEqual numerOfItems
                bookList.forEachIndexed { index, book ->
                    dataItems[index].let {
                        book.title shouldEqual it.title
                        book.author shouldEqual it.author
                        book.image shouldEqual it.image
                        book.shortSynopsis shouldEqual it.shortSynopsis
                        book.synopsis shouldEqual it.synopsis
                    }
                }
            })

            verify(bookRepository, Times(1)).getBooks()
        }
    }

    @Test
    fun `check return failure when repository returns failure`() {
        runBlocking {
            whenever(bookRepository.getBooks()).thenReturn(Left(Failure.GenericError))
            getBooks.run(Unit)
                .fold({ it shouldBeInstanceOf Failure.GenericError::class }, { assert(false) })

            verify(bookRepository, Times(1)).getBooks()
        }
    }
}
