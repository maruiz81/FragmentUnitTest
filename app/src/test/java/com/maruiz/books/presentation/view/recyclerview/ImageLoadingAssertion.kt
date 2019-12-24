package com.maruiz.books.presentation.view.recyclerview

import android.view.View
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import coil.ImageLoader
import coil.request.LoadRequest
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.verify

class ImageLoadingAssertion(
    private val urlImage: String,
    private val imageLoader: ImageLoader
) : ViewAssertion {
    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        argumentCaptor<LoadRequest>().run {
            verify(imageLoader, atLeastOnce()).load(capture())
            assert(allValues.any { it.data == urlImage })
        }
    }
}