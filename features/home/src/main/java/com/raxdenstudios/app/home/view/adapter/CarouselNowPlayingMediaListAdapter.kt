package com.raxdenstudios.app.home.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raxdenstudios.app.base.BaseListAdapter
import com.raxdenstudios.app.home.databinding.CarouselNowPlayingMediaListItemBinding
import com.raxdenstudios.app.media.view.model.MediaListItemModel

internal class CarouselNowPlayingMediaListAdapter :
    BaseListAdapter<MediaListItemModel, CarouselNowPlayingMediaListAdapter.CarouselNowPlayingMediaListAdapterHolder>(
        areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id }
    ) {

    var onMediaClickListener: (MediaListItemModel) -> Unit = {}
    var onMediaPlayClickListener: (MediaListItemModel) -> Unit = {}
    var onWatchListClickListener: (MediaListItemModel) -> Unit = {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CarouselNowPlayingMediaListAdapterHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CarouselNowPlayingMediaListItemBinding.inflate(inflater, parent, false)
        return CarouselNowPlayingMediaListAdapterHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselNowPlayingMediaListAdapterHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CarouselNowPlayingMediaListAdapterHolder(
        private val binding: CarouselNowPlayingMediaListItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MediaListItemModel) {
            binding.populate(item)
        }

        private fun CarouselNowPlayingMediaListItemBinding.populate(item: MediaListItemModel) {
            itemView.onMediaClickListener = onMediaClickListener
            itemView.onMediaPlayClickListener = onMediaPlayClickListener
            itemView.onWatchListClickListener = { onWatchListClickListener(item) }
            itemView.setModel(item)
        }
    }
}
