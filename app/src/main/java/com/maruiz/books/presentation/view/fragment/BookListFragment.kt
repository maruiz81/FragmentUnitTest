package com.maruiz.books.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.maruiz.books.R
import com.maruiz.books.presentation.adapter.BooksAdapter
import com.maruiz.books.presentation.utils.observeEvent
import com.maruiz.books.presentation.viewmodel.BooksViewModel
import kotlinx.android.synthetic.main.fragment_book_list.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

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

        addViewModelObservers()
    }

    private fun addViewModelObservers() {
        booksViewModel.run {
            observeBooks().observe(viewLifecycleOwner, Observer {
                adapter.renderables = it
            })
            observeFailure().observe(viewLifecycleOwner, Observer {
                Toast.makeText(context, R.string.error, Toast.LENGTH_LONG).show()
            })
            requestBooks()

            navigateToDetail().observeEvent(viewLifecycleOwner) {
                get<NavController> { parametersOf(this@BookListFragment) }.navigate(
                    BookListFragmentDirections.listToDetail(
                        it
                    )
                )
            }
        }
    }

    private fun setupRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter.apply { itemClicked = { booksViewModel.bookSelected(it) } }
    }
}