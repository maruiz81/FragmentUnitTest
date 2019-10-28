package com.maruiz.arrowexample.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.maruiz.arrowexample.R
import com.maruiz.arrowexample.presentation.extensions.loadImage
import com.maruiz.arrowexample.presentation.presentationmodel.BookPresentationModel
import kotlinx.android.synthetic.main.fragment_book_detail.description
import kotlinx.android.synthetic.main.fragment_book_detail.collapsing_toolbar_layout as collapsingToolbarLayout
import kotlinx.android.synthetic.main.fragment_book_detail.toolbar_image as toolbarImage

class BookDetailFragment : Fragment() {

    private val args by navArgs<BookDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_book_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView(args.book)
    }

    private fun setupView(book: BookPresentationModel) {
        toolbarImage.loadImage(book.image)
        description.text = book.synopsis
        collapsingToolbarLayout.title = book.title
    }
}