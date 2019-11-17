package com.maruiz.books.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import coil.ImageLoader
import coil.api.load
import com.maruiz.books.R
import com.maruiz.books.presentation.presentationmodel.BookPresentationModel
import com.maruiz.books.presentation.viewmodel.BookDetailViewModel
import kotlinx.android.synthetic.main.fragment_book_detail.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.android.synthetic.main.fragment_book_detail.collapsing_toolbar_layout as collapsingToolbarLayout
import kotlinx.android.synthetic.main.fragment_book_detail.toolbar_image as toolbarImage

class BookDetailFragment : Fragment() {

    private val bookDetailViewModel: BookDetailViewModel by viewModel()

    private val imageLoader: ImageLoader by inject()

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
        bookDetailViewModel.observeBook().observe(viewLifecycleOwner, Observer {
            toolbarImage.load(it.image, imageLoader)
            description.text = it.synopsis
            collapsingToolbarLayout.title = it.title
        })
        bookDetailViewModel.showBook(book)
    }
}