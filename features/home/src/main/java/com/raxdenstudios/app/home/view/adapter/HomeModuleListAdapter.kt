package com.raxdenstudios.app.home.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.raxdenstudios.app.base.BaseListAdapter
import com.raxdenstudios.app.home.databinding.HomeCarouselMediasModuleListItemBinding
import com.raxdenstudios.app.home.databinding.HomeCarouselNowPlayingModuleListItemBinding
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.app.media.view.model.MediaListItemModel

internal class HomeModuleListAdapter :
  BaseListAdapter<HomeModuleModel, HomeModuleListAdapterHolder>(
    areItemsTheSame = { oldItem, newItem -> oldItem == newItem }
  ) {

  private enum class ViewType {
    CAROUSEL_MEDIAS_LAYOUT, NOW_PLAYING_LAYOUT
  }

  var onCarouselAddToWatchListClickListener: (
    HomeModuleModel.CarouselMedias,
    MediaListItemModel
  ) -> Unit = { _, _ -> }
  var onCarouselRemoveFromWatchListClickListener: (
    HomeModuleModel.CarouselMedias,
    MediaListItemModel
  ) -> Unit = { _, _ -> }
  var onCarouselMediaClickListener: (
    HomeModuleModel.CarouselMedias,
    MediaListItemModel
  ) -> Unit = { _, _ -> }
  var onCarouselSeeAllClickListener: (
    HomeModuleModel.CarouselMedias,
  ) -> Unit = { _ -> }
  var onCarouselFilterChanged: (
    HomeModuleModel.CarouselMedias,
    MediaType
  ) -> Unit = { _, _ -> }

  override fun getItemId(position: Int): Long = getItem(position).getModuleItemId()

  override fun getItemViewType(position: Int): Int = when (getItem(position)) {
    is HomeModuleModel.CarouselMedias.NowPlaying -> ViewType.NOW_PLAYING_LAYOUT.ordinal
    else -> ViewType.CAROUSEL_MEDIAS_LAYOUT.ordinal
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeModuleListAdapterHolder {
    val inflater = LayoutInflater.from(parent.context)
    val holder = when (ViewType.values()[viewType]) {
      ViewType.NOW_PLAYING_LAYOUT -> {
        val binding = HomeCarouselNowPlayingModuleListItemBinding.inflate(inflater, parent, false)
        HomeModuleListAdapterHolder.HomeCarouselNowPlayingModuleListAdapter(binding)
      }
      ViewType.CAROUSEL_MEDIAS_LAYOUT -> {
        val binding = HomeCarouselMediasModuleListItemBinding.inflate(inflater, parent, false)
        HomeModuleListAdapterHolder.HomeCarouselMediasModuleListAdapterHolder(binding)
      }
    }
    return holder
  }

  override fun onBindViewHolder(holder: HomeModuleListAdapterHolder, position: Int) =
    when (holder) {
      is HomeModuleListAdapterHolder.HomeCarouselMediasModuleListAdapterHolder -> {
        holder.onCarouselAddToWatchListClickListener = onCarouselAddToWatchListClickListener
        holder.onCarouselRemoveFromWatchListClickListener =
          onCarouselRemoveFromWatchListClickListener
        holder.onCarouselMediaClickListener = onCarouselMediaClickListener
        holder.onCarouselSeeAllClickListener = onCarouselSeeAllClickListener
        holder.onCarouselFilterChanged = onCarouselFilterChanged
        holder.bind(getItem(position) as HomeModuleModel.CarouselMedias)
      }
      is HomeModuleListAdapterHolder.HomeCarouselNowPlayingModuleListAdapter -> {
        holder.onCarouselAddToWatchListClickListener = onCarouselAddToWatchListClickListener
        holder.onCarouselRemoveFromWatchListClickListener =
          onCarouselRemoveFromWatchListClickListener
        holder.onCarouselMediaClickListener = onCarouselMediaClickListener
        holder.onCarouselSeeAllClickListener = onCarouselSeeAllClickListener
        holder.bind(getItem(position) as HomeModuleModel.CarouselMedias.NowPlaying)
      }
    }
}
