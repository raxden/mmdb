package com.raxdenstudios.app.list.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raxdenstudios.app.base.BaseListAdapter
import com.raxdenstudios.app.list.R
import com.raxdenstudios.app.movie.view.component.MovieListItemView
import com.raxdenstudios.app.movie.view.model.MediaListItemModel
import com.raxdenstudios.commons.ext.setSafeOnClickListener

internal class MovieListAdapter :
  BaseListAdapter<MediaListItemModel, MovieListAdapter.MovieListAdapterHolder>(
    areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id }
  ) {

  var onAddMovieToWatchListClickListener: (MediaListItemModel) -> Unit = {}
  var onRemoveMovieFromWatchListClickListener: (MediaListItemModel) -> Unit = {}
  var onMovieClickListener: (MediaListItemModel) -> Unit = {}

  override fun getItemViewType(position: Int) = R.layout.movie_list_item

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListAdapterHolder {
    val view = inflateView(parent, viewType)
    return MovieListAdapterHolder(view)
  }

  private fun inflateView(parent: ViewGroup, viewType: Int) =
    LayoutInflater.from(parent.context).inflate(viewType, parent, false)

  override fun onBindViewHolder(holder: MovieListAdapterHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class MovieListAdapterHolder(
    private val view: View
  ) : RecyclerView.ViewHolder(view) {

    fun bind(item: MediaListItemModel) {
      val component = view.findViewById<MovieListItemView>(R.id.item_view)
      component.setSafeOnClickListener { onMovieClickListener(item) }
      component.onAddToWatchListClickListener = { onAddMovieToWatchListClickListener(item) }
      component.onRemoveFromWatchListClickListener =
        { onRemoveMovieFromWatchListClickListener(item) }
      component.setModel(item)
    }
  }
}