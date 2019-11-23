package com.maruiz.books.presentation.view

import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import coil.ImageLoader
import coil.request.LoadRequest
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.verify
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.core.Is

fun atPosition(position: Int, itemMatcher: Matcher<View>, @IdRes targetViewId: Int): Matcher<View> =
    object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description?) {
            description!!.appendText("has item at position $position: ")
            itemMatcher.describeTo(description)
        }

        override fun matchesSafely(view: RecyclerView?): Boolean =
            view?.findViewHolderForAdapterPosition(position)?.run {
                itemView.findViewById<TextView>(targetViewId).let {
                    itemMatcher.matches(it)
                }
            } ?: false
    }

fun imageAtPosition(
    position: Int,
    urlImage: String,
    imageLoader: ImageLoader
): Matcher<View> =
    object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description?) {
            description!!.appendText("has Image at position $position: ")
        }

        override fun matchesSafely(view: RecyclerView?): Boolean =
            argumentCaptor<LoadRequest>().run {
                verify(imageLoader, atLeastOnce()).load(capture())
                allValues[position].data == urlImage
            }
    }

class RecyclerViewItemCountAssertion(private val expectedCount: Int) : ViewAssertion {

    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        ViewMatchers.assertThat(adapter!!.itemCount, Is.`is`(expectedCount))
    }
}