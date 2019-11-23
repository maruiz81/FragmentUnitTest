package com.maruiz.books.presentation.view

import androidx.test.espresso.matcher.BoundedMatcher
import com.google.android.material.appbar.CollapsingToolbarLayout
import org.hamcrest.Description
import org.hamcrest.Matcher

fun withCollapsibleToolbarTitle(textMatcher: Matcher<String>): Matcher<Any> = object :
    BoundedMatcher<Any, CollapsingToolbarLayout>(CollapsingToolbarLayout::class.java) {
    override fun describeTo(description: Description) {
        description.appendText("with toolbar title: ")
        textMatcher.describeTo(description)
    }

    override fun matchesSafely(toolbarLayout: CollapsingToolbarLayout): Boolean =
        textMatcher.matches(toolbarLayout.title)
}
