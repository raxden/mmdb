package com.raxdenstudios.app.home.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raxdenstudios.app.base.BaseListAdapter
import com.raxdenstudios.app.home.R
import com.raxdenstudios.app.home.view.component.HomeCarouselMediasModuleView
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.app.media.view.model.MediaListItemModel

internal class HomeModuleListAdapter :
  BaseListAdapter<HomeModuleModel, HomeModuleListAdapter.HomeListAdapterHolder>(
    areItemsTheSame = { oldItem, newItem -> oldItem == newItem }
  ) {

  companion object {
    private val CAROUSEL_MOVIES_LAYOUT = R.layout.home_carousel_medias_module_list_item
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
  var onCarouselSigInClickListener: (HomeModuleModel.CarouselMedias) -> Unit = {}

  override fun getItemId(position: Int): Long = getItem(position).getModuleItemId()

  override fun getItemViewType(position: Int): Int = when (getItem(position)) {
    is HomeModuleModel.CarouselMedias -> CAROUSEL_MOVIES_LAYOUT
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListAdapterHolder {
    val view = inflateView(parent, viewType)
    return HomeListAdapterHolder(view)
  }

  private fun inflateView(parent: ViewGroup, viewType: Int) =
    LayoutInflater.from(parent.context).inflate(viewType, parent, false)

  override fun onBindViewHolder(holder: HomeListAdapterHolder, position: Int) =
    holder.bind(getItem(position))

  inner class HomeListAdapterHolder(
    private val view: View
  ) : RecyclerView.ViewHolder(view) {

    fun bind(model: HomeModuleModel) = when (model) {
      is HomeModuleModel.CarouselMedias -> bindCarousel(model)
    }

    private fun bindCarousel(model: HomeModuleModel.CarouselMedias) {
      view.findViewById<HomeCarouselMediasModuleView>(R.id.item_view).run {
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
        onSigInClickListener = onCarouselSigInClickListener
        setModel(model)
      }
    }
  }
}
