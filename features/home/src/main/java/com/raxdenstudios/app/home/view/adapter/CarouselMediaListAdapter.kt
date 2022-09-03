package com.raxdenstudios.app.home.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raxdenstudios.app.base.BaseListAdapter
import com.raxdenstudios.app.home.databinding.CarouselMediaListItemBinding
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.commons.ext.setSafeOnClickListener

internal class CarouselMediaListAdapter :
    BaseListAdapter<MediaListItemModel, CarouselMediaListAdapter.MediaListAdapterHolder>(
        areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id }
    ) {

    var onAddToWatchListClickListener: (MediaListItemModel) -> Unit = {}
    var onRemoveFromWatchListClickListener: (MediaListItemModel) -> Unit = {}
    var onClickListener: (MediaListItemModel) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaListAdapterHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CarouselMediaListItemBinding.inflate(inflater, parent, false)
        return MediaListAdapterHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaListAdapterHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MediaListAdapterHolder(
        private val binding: CarouselMediaListItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MediaListItemModel) {
            binding.populate(item)
        }

        private fun CarouselMediaListItemBinding.populate(item: MediaListItemModel) {
            itemView.setSafeOnClickListener { onClickListener(item) }
            itemView.onAddToWatchListClickListener = { onAddToWatchListClickListener(item) }
            itemView.onRemoveFromWatchListClickListener = { onRemoveFromWatchListClickListener(item) }
            itemView.setModel(item)
        }
    }
}
