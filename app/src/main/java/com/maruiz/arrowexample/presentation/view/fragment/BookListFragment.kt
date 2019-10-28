package com.maruiz.arrowexample.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.maruiz.arrowexample.R
import com.maruiz.arrowexample.presentation.adapter.BooksAdapter
import com.maruiz.arrowexample.presentation.utils.observeEvent
import com.maruiz.arrowexample.presentation.viewmodel.BooksViewModel
import kotlinx.android.synthetic.main.fragment_book_list.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookListFragment : Fragment() {

    private val adapter: BooksAdapter by inject()

    private val booksViewModel: BooksViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_book_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()

        booksViewModel.observeBooks().observe(this, Observer {
            adapter.renderables = it
        })
        booksViewModel.observeFailure().observe(this, Observer {
            Toast.makeText(context, R.string.error, Toast.LENGTH_LONG).show()
        })
        booksViewModel.requestBooks()

        booksViewModel.navigateToDetail().observeEvent(this) {
            findNavController().navigate(BookListFragmentDirections.listToDetail(it))
        }
    }

    private fun setupRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter.apply { itemClicked = { booksViewModel.bookSelected(it) } }
    }
}