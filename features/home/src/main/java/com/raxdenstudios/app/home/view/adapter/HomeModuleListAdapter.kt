package com.raxdenstudios.app.home.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raxdenstudios.app.base.BaseListAdapter
import com.raxdenstudios.app.home.R
import com.raxdenstudios.app.home.view.component.CarouselMediaListView
import com.raxdenstudios.app.home.view.model.CarouselMediaListModel
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.commons.ext.setSafeOnClickListener

internal class HomeModuleListAdapter :
  BaseListAdapter<HomeModuleModel, HomeModuleListAdapter.HomeListAdapterHolder>(
    areItemsTheSame = { oldItem, newItem -> oldItem == newItem }
  ) {

  companion object {
    private val CAROUSEL_MOVIES_LAYOUT = R.layout.home_module_list_item
    private val WATCHLIST_NOT_LOGGED = R.layout.sigin_watch_list_view
    private val WATCHLIST_WITHOUT_CONTENT = R.layout.empty_watch_list_view
  }

  var onCarouselAddToWatchListClickListener: (
    HomeModuleModel.CarouselMedias,
    CarouselMediaListModel,
    MediaListItemModel
  ) -> Unit = { _, _, _ -> }
  var onCarouselRemoveFromWatchListClickListener: (
    HomeModuleModel.CarouselMedias,
    CarouselMediaListModel,
    MediaListItemModel
  ) -> Unit = { _, _, _ -> }
  var onCarouselMediaClickListener: (
    HomeModuleModel.CarouselMedias,
    CarouselMediaListModel,
    MediaListItemModel
  ) -> Unit = { _, _, _ -> }
  var onCarouselSeeAllClickListener: (
    HomeModuleModel.CarouselMedias,
    CarouselMediaListModel
  ) -> Unit = { _, _ -> }
  var onSigInClickListener: () -> Unit = {}

  override fun getItemId(position: Int): Long = getItem(position).getModuleItemId()

  override fun getItemViewType(position: Int): Int = when (getItem(position)) {
    is HomeModuleModel.CarouselMedias -> CAROUSEL_MOVIES_LAYOUT
    HomeModuleModel.WatchlistNotLogged -> WATCHLIST_NOT_LOGGED
    HomeModuleModel.WatchlistWithoutContent -> WATCHLIST_WITHOUT_CONTENT
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
      is HomeModuleModel.CarouselMedias -> bindCarousel(model, model.carouselMediaListModel)
      HomeModuleModel.WatchlistNotLogged -> bindWatchListNotLogged()
      HomeModuleModel.WatchlistWithoutContent -> bindWatchListWithoutContent()
    }

    private fun bindWatchListWithoutContent() {
    }

    private fun bindWatchListNotLogged() {
      view.findViewById<View>(R.id.sig_in).setSafeOnClickListener { onSigInClickListener() }
    }

    private fun bindCarousel(model: HomeModuleModel.CarouselMedias, item: CarouselMediaListModel) {
      val component = view.findViewById<CarouselMediaListView>(R.id.item_view)
      component.onSeeAllClickListener = { carouselMediasModel ->
        onCarouselSeeAllClickListener(model, carouselMediasModel)
      }
      component.onMediaClickListener = { carouselMediasModel, mediaItemModel ->
        onCarouselMediaClickListener(model, carouselMediasModel, mediaItemModel)
      }
      component.onAddToWatchListClickListener = { carouselMediaListModel, mediaListItemModel ->
        onCarouselAddToWatchListClickListener(model, carouselMediaListModel, mediaListItemModel)
      }
      component.onRemoveFromWatchListClickListener = { carouselMediaListModel, mediaListItemModel ->
        onCarouselRemoveFromWatchListClickListener(
          model,
          carouselMediaListModel,
          mediaListItemModel
        )
      }
      component.setModel(item)
    }
  }
}
