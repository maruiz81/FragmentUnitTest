package com.maruiz.books.presentation.view.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil.DefaultRequestOptions
import coil.ImageLoader
import coil.request.LoadRequest
import coil.util.CoilContentProvider
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.maruiz.books.R
import com.maruiz.books.presentation.presentationmodel.BookPresentationModel
import com.maruiz.books.presentation.view.withCollapsibleToolbarTitle
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import org.hamcrest.core.Is.`is`
import org.junit.After
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
class BookDetailFragmentTest {
    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    private lateinit var sut: FragmentScenario<BookDetailFragment>

    @Mock
    private lateinit var imageLoader: ImageLoader

    @Before
    fun setup() {
        Robolectric.setupContentProvider(CoilContentProvider::class.java)
        loadKoinModules(module(override = true) {
            factory { imageLoader }
        })
        whenever(imageLoader.defaults).thenReturn(DefaultRequestOptions())
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun `load properly the fragment`() {
        sut = launchFragmentInContainer<BookDetailFragment>(themeResId = R.style.AppTheme,
            fragmentArgs = Bundle()
                .apply {
                    putParcelable("book", BookPresentationModel(title = "Salem's lot"))
                }
        )
    }

    @Test
    fun `show the data from the arguments`() {
        val bookParameter = BookPresentationModel(
            title = "Salem's lot",
            image = "image url", synopsis = "This is the synopsis to"
        )
        sut = launchFragmentInContainer<BookDetailFragment>(themeResId = R.style.AppTheme,
            fragmentArgs = Bundle()
                .apply { putParcelable("book", bookParameter) }
        )

        argumentCaptor<LoadRequest>().apply {
            verify(imageLoader).load(capture())
            assertEquals(firstValue.data, bookParameter.image)
        }

        onView(isAssignableFrom(CollapsingToolbarLayout::class.java)).check(
            matches(
                withCollapsibleToolbarTitle(
                    `is`(bookParameter.title)
                )
            )
        )

        onView(withId(R.id.description)).check(matches(withText(bookParameter.synopsis)))
    }
}