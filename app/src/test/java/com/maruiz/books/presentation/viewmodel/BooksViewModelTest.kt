package com.maruiz.books.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import com.maruiz.books.data.di.dataModule
import com.maruiz.books.data.error.Failure
import com.maruiz.books.data.getBookListDomain
import com.maruiz.books.domain.model.BookModelDomainModel
import com.maruiz.books.domain.usecases.GetBooks
import com.maruiz.books.presentation.di.presentationModule
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
                    dataModule,
                    presentationModule,
                    module(override = true) {
                        factory { getBooks }
                    }
                ))
        }
    }

    @Test
    fun `verify useCase executed`() {
        booksViewModel.requestBooks()

        verify(getBooks, Times(1)).invoke(any(), any(), any())
    }

    @Test
    fun `get a list of books empty`() {
        whenever(getBooks.invoke(any(), any(), any())).thenAnswer { arg ->
            arg.getArgument<(Either<Failure, List<BookModelDomainModel>>) -> Unit>(2)(
                Right(
                    emptyList()
                )
            )
        }

        booksViewModel.requestBooks()

        booksViewModel.observeBooks().value!!.size shouldEqual 0
    }

    @Test
    fun `get properly list of ten Books`() {
        val itemsNumber = 10
        val bookList = getBookListDomain(itemsNumber)
        whenever(getBooks.invoke(any(), any(), any())).thenAnswer { arg ->
            arg.getArgument<(Either<Failure, List<BookModelDomainModel>>) -> Unit>(2)(Right(bookList))
        }
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
        whenever(getBooks.invoke(any(), any(), any())).thenAnswer { arg ->
            arg.getArgument<(Either<Failure, List<BookModelDomainModel>>) -> Unit>(2)(Left(Failure.GenericError))
        }

        booksViewModel.requestBooks()

        booksViewModel.observeFailure().value!!.run {
            this shouldEqual Failure.GenericError
        }
    }
}