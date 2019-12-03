package com.maruiz.books.presentation.view.fragment

import android.os.Build
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil.DefaultRequestOptions
import coil.ImageLoader
import coil.util.CoilContentProvider
import com.maruiz.books.R
import com.maruiz.books.data.error.Failure
import com.maruiz.books.presentation.adapter.BooksAdapter
import com.maruiz.books.presentation.presentationmodel.BookPresentationModel
import com.maruiz.books.presentation.utils.Event
import com.maruiz.books.presentation.view.RecyclerViewItemCountAssertion
import com.maruiz.books.presentation.view.atPosition
import com.maruiz.books.presentation.view.imageAtPosition
import com.maruiz.books.presentation.viewmodel.BooksViewModel
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness
import org.robolectric.Robolectric
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class BookListFragmentTest {
    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    private lateinit var sut: FragmentScenario<BookListFragment>

    @Mock
    private lateinit var viewModel: BooksViewModel

    @Mock
    private lateinit var navController: NavController

    @Mock
    private lateinit var imageLoader: ImageLoader

    @Before
    fun setup() {
        Robolectric.setupContentProvider(CoilContentProvider::class.java)
        loadKoinModules(
            module(override = true) {
                factory { viewModel }
                factory { navController }
                factory { imageLoader }
            }
        )
    }

    @After
    fun autoClose() {
        stopKoin()
    }

    @Test(expected = Test.None::class)
    fun startWithoutCrashing() {
        setupObservers()
        sut = launchFragmentInContainer()
    }

    @Test
    fun `Check the list is empty`() {
        setupObservers()

        sut = launchFragmentInContainer()

        onView(withId(R.id.recyclerView)).check(RecyclerViewItemCountAssertion(0))
    }

    @Test
    fun `Check the list should contains 5 items`() {
        val books = MutableLiveData<List<BookPresentationModel>>()
        setupObservers(books)

        sut = launchFragmentInContainer()

        whenever(imageLoader.defaults).thenReturn(DefaultRequestOptions())

        val itemNumber = 5
        books.value = getBookList(itemNumber)

        onView(withId(R.id.recyclerView)).check(RecyclerViewItemCountAssertion(itemNumber))
    }

    @Test
    fun `check item is introduced in the list`() {
        val bookList = MutableLiveData<List<BookPresentationModel>>()
        val direction = MutableLiveData<Event<BookPresentationModel>>()
        setupObservers(bookList, navigateLiveData = direction)

        sut = launchFragmentInContainer()

        whenever(imageLoader.defaults).thenReturn(DefaultRequestOptions())

        getBookList(10)
            .also { bookList.value = it }
            .forEachIndexed { index, item ->
                onView(withId(R.id.recyclerView))
                    .perform(scrollToPosition<BooksAdapter.ViewHolder>(index))
                    .check(
                        matches(
                            atPosition(
                                index,
                                withText(item.title),
                                R.id.title
                            )
                        )
                    )
                    .check(
                        matches(
                            atPosition(
                                index,
                                withText(item.author),
                                R.id.author
                            )
                        )
                    )
                    .check(
                        matches(
                            atPosition(
                                index,
                                withText(item.shortSynopsis),
                                R.id.synopsis
                            )
                        )
                    )
                    .check(
                        matches(
                            imageAtPosition(
                                index,
                                item.image,
                                imageLoader
                            )
                        )
                    )
            }
    }

    @Test
    fun `check when click in a element of the list`() {
        val bookLiveData = MutableLiveData<List<BookPresentationModel>>()
        val direction = MutableLiveData<Event<BookPresentationModel>>()
        setupObservers(bookLiveData, navigateLiveData = direction)

        sut = launchFragmentInContainer()

        whenever(imageLoader.defaults).thenReturn(DefaultRequestOptions())

        val bookList = getBookList(10)
        bookLiveData.value = bookList

        bookList.forEachIndexed { index, book ->
            onView(withId(R.id.recyclerView))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<BooksAdapter.ViewHolder>(
                        index,
                        click()
                    )
                )

            verify(viewModel).bookSelected(book)

            direction.value = Event(book)

            argumentCaptor<NavDirections>().apply {
                verify(navController, atLeastOnce()).navigate(capture())
                assertEquals(
                    BookListFragmentDirections.listToDetail(book),
                    lastValue
                )
            }
        }
    }

    private fun setupObservers(
        booksLiveData: MutableLiveData<List<BookPresentationModel>> = MutableLiveData(),
        failureLiveData: MutableLiveData<Failure> = MutableLiveData(),
        navigateLiveData: MutableLiveData<Event<BookPresentationModel>> = MutableLiveData()
    ) {
        whenever(viewModel.observeBooks()).thenReturn(booksLiveData)
        whenever(viewModel.observeFailure()).thenReturn(failureLiveData)
        whenever(viewModel.navigateToDetail()).thenReturn(navigateLiveData)
    }

    private fun getBookList(size: Int): List<BookPresentationModel> =
        (1..size).map {
            BookPresentationModel(
                "Book $it", "Author $it", "Short Synopsis $it",
                "Synopsis $it", "image url $it"
            )
        }
}
