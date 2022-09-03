package com.raxdenstudios.app.home.view.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.raxdenstudios.app.home.databinding.HomeCarouselMediasModuleListItemBinding
import com.raxdenstudios.app.home.databinding.HomeCarouselNowPlayingModuleListItemBinding
import com.raxdenstudios.app.home.view.component.HomeCarouselMediasModuleView
import com.raxdenstudios.app.home.view.component.HomeCarouselNowPlayingMediasModuleView
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.app.media.view.model.MediaListItemModel

sealed class HomeModuleListAdapterHolder(
    binding: ViewBinding,
) : RecyclerView.ViewHolder(binding.root) {

    var onCarouselAddToWatchListClickListener: (
        HomeModuleModel.CarouselMedias,
        MediaListItemModel,
    ) -> Unit = { _, _ -> }
    var onCarouselRemoveFromWatchListClickListener: (
        HomeModuleModel.CarouselMedias,
        MediaListItemModel,
    ) -> Unit = { _, _ -> }
    var onCarouselMediaClickListener: (
        HomeModuleModel.CarouselMedias,
        MediaListItemModel,
    ) -> Unit = { _, _ -> }
    var onCarouselSeeAllClickListener: (
        HomeModuleModel.CarouselMedias,
    ) -> Unit = { _ -> }

    class HomeCarouselMediasModuleListAdapterHolder(
        private val binding: HomeCarouselMediasModuleListItemBinding,
    ) : HomeModuleListAdapterHolder(binding) {

        var onCarouselFilterChanged: (
            HomeModuleModel.CarouselMedias,
            MediaType,
        ) -> Unit = { _, _ -> }

        fun bind(model: HomeModuleModel.CarouselMedias) {
            binding.itemView.populate(model)
        }

        private fun HomeCarouselMediasModuleView.populate(model: HomeModuleModel.CarouselMedias) {
            onSeeAllClickListener = { carouselMedias ->
                onCarouselSeeAllClickListener(carouselMedias)
            }
            onMediaClickListener = { carouselMedias, mediaItemModel ->
                onCarouselMediaClickListener(carouselMedias, mediaItemModel)
            }
            onAddToWatchListClickListener = { carouselMedias, mediaListItemModel ->
                onCarouselAddToWatchListClickListener(carouselMedias, mediaListItemModel)
            }
            onRemoveFromWatchListClickListener = { carouselMedias, mediaListItemModel ->
                onCarouselRemoveFromWatchListClickListener(carouselMedias, mediaListItemModel)
            }
            onMediaTypeFilterChanged = { carouselMedias, mediaFilterModel ->
                onCarouselFilterChanged(carouselMedias, mediaFilterModel)
            }
            setModel(model)
        }
    }

    class HomeCarouselNowPlayingModuleListAdapter(
        private val binding: HomeCarouselNowPlayingModuleListItemBinding,
    ) : HomeModuleListAdapterHolder(binding) {

        fun bind(model: HomeModuleModel.CarouselMedias.NowPlaying) {
            binding.itemView.populate(model)
        }

        private fun HomeCarouselNowPlayingMediasModuleView.populate(model: HomeModuleModel.CarouselMedias.NowPlaying) {
            onSeeAllClickListener = { carouselMedias ->
                onCarouselSeeAllClickListener(carouselMedias)
            }
            onMediaClickListener = { carouselMedias, mediaItemModel ->
                onCarouselMediaClickListener(carouselMedias, mediaItemModel)
            }
            onAddToWatchListClickListener = { carouselMedias, mediaListItemModel ->
                onCarouselAddToWatchListClickListener(carouselMedias, mediaListItemModel)
            }
            onRemoveFromWatchListClickListener = { carouselMedias, mediaListItemModel ->
                onCarouselRemoveFromWatchListClickListener(carouselMedias, mediaListItemModel)
            }
            setModel(model)
        }
    }
}
