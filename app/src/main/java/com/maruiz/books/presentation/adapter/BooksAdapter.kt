package com.maruiz.books.presentation.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.api.load
import com.maruiz.books.R
import com.maruiz.books.presentation.extensions.inflate
import com.maruiz.books.presentation.presentationmodel.BookPresentationModel
import kotlinx.android.synthetic.main.row_book.view.*
import kotlin.properties.Delegates

class BooksAdapter(private val imageLoader: ImageLoader) :
    RecyclerView.Adapter<BooksAdapter.ViewHolder>() {
    var renderables: List<BookPresentationModel> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    lateinit var itemClicked: (BookPresentationModel) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.inflate(R.layout.row_book))

    override fun getItemCount(): Int = renderables.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(renderables[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(model: BookPresentationModel) {
            itemView.image.load(model.image, imageLoader)
            itemView.title.text = model.title
            itemView.author.text = model.author
            itemView.synopsis.text = model.shortSynopsis
            itemView.setOnClickListener { itemClicked(model) }
        }
    }
}