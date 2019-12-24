package com.maruiz.books.presentation.view.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import org.hamcrest.Matcher

class RecyclerViewInteraction<A>(
    private val viewMatcher: Matcher<View>,
    private val items: List<A>
) {
    fun check(assertion: (item: A, view: View, e: NoMatchingViewException?) -> Unit) {
        items.mapIndexed { index, item ->
            onView(viewMatcher)
                .perform(scrollToPosition<RecyclerView.ViewHolder>(index))
                .check(RecyclerItemViewAssertion(index, item, assertion))
        }
    }
}