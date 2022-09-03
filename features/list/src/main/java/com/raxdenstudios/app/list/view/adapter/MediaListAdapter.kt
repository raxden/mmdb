package com.raxdenstudios.app.list.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raxdenstudios.app.base.BaseListAdapter
import com.raxdenstudios.app.list.databinding.MediaListItemBinding
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.commons.ext.setSafeOnClickListener

internal class MediaListAdapter :
    BaseListAdapter<MediaListItemModel, MediaListAdapter.MediaListAdapterHolder>(
        areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id }
    ) {

    var onAddMovieToWatchListClickListener: (MediaListItemModel) -> Unit = {}
    var onRemoveMovieFromWatchListClickListener: (MediaListItemModel) -> Unit = {}
    var onMovieClickListener: (MediaListItemModel) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaListAdapterHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MediaListItemBinding.inflate(inflater, parent, false)
        return MediaListAdapterHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaListAdapterHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MediaListAdapterHolder(
        private val binding: MediaListItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MediaListItemModel) {
            binding.populate(item)
        }

        private fun MediaListItemBinding.populate(item: MediaListItemModel) {
            itemView.setSafeOnClickListener { onMovieClickListener(item) }
            itemView.onAddToWatchListClickListener = { onAddMovieToWatchListClickListener(item) }
            itemView.onRemoveFromWatchListClickListener =
                { onRemoveMovieFromWatchListClickListener(item) }
            itemView.setModel(item)
        }
    }
}
