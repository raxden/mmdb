package com.raxdenstudios.app.list.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raxdenstudios.app.base.BaseListAdapter
import com.raxdenstudios.app.list.R
import com.raxdenstudios.app.movie.view.component.MediaListItemView
import com.raxdenstudios.app.movie.view.model.MediaListItemModel
import com.raxdenstudios.commons.ext.setSafeOnClickListener

internal class MediaListAdapter :
  BaseListAdapter<MediaListItemModel, MediaListAdapter.MediaListAdapterHolder>(
    areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id }
  ) {

  var onAddMovieToWatchListClickListener: (MediaListItemModel) -> Unit = {}
  var onRemoveMovieFromWatchListClickListener: (MediaListItemModel) -> Unit = {}
  var onMovieClickListener: (MediaListItemModel) -> Unit = {}

  override fun getItemViewType(position: Int) = R.layout.movie_list_item

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaListAdapterHolder {
    val view = inflateView(parent, viewType)
    return MediaListAdapterHolder(view)
  }

  private fun inflateView(parent: ViewGroup, viewType: Int) =
    LayoutInflater.from(parent.context).inflate(viewType, parent, false)

  override fun onBindViewHolder(holder: MediaListAdapterHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class MediaListAdapterHolder(
    private val view: View
  ) : RecyclerView.ViewHolder(view) {

    fun bind(item: MediaListItemModel) {
      val component = view.findViewById<MediaListItemView>(R.id.item_view)
      component.setSafeOnClickListener { onMovieClickListener(item) }
      component.onAddToWatchListClickListener = { onAddMovieToWatchListClickListener(item) }
      component.onRemoveFromWatchListClickListener =
        { onRemoveMovieFromWatchListClickListener(item) }
      component.setModel(item)
    }
  }
}