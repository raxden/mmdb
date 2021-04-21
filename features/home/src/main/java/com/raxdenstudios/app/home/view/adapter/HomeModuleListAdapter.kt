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
import com.raxdenstudios.app.movie.view.model.MediaFilterModel
import com.raxdenstudios.app.movie.view.model.MediaListItemModel
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

  var onAddMovieToWatchListClickListener: (HomeModuleModel.CarouselMovies, CarouselMediaListModel, MediaListItemModel) -> Unit =
    { _, _, _ -> }
  var onRemoveMovieFromWatchListClickListener: (HomeModuleModel.CarouselMovies, CarouselMediaListModel, MediaListItemModel) -> Unit =
    { _, _, _ -> }
  var onMovieClickListener: (HomeModuleModel.CarouselMovies, CarouselMediaListModel, MediaListItemModel) -> Unit =
    { _, _, _ -> }
  var onCarouselMoviesModel: (HomeModuleModel.CarouselMovies, CarouselMediaListModel) -> Unit =
    { _, _ -> }
  var onSigInClickListener: () -> Unit = {}

  override fun getItemId(position: Int): Long = when (val item = getItem(position)) {
    is HomeModuleModel.CarouselMovies -> when (item.mediaFilterModel) {
      is MediaFilterModel.NowPlaying -> 1
      is MediaFilterModel.Popular -> 2
      is MediaFilterModel.TopRated -> 3
      MediaFilterModel.Upcoming -> 4
      is MediaFilterModel.WatchList -> 5
    }
    HomeModuleModel.WatchlistNotLogged -> 6
    HomeModuleModel.WatchlistWithoutContent -> 7
  }

  override fun getItemViewType(position: Int): Int = when (getItem(position)) {
    is HomeModuleModel.CarouselMovies -> CAROUSEL_MOVIES_LAYOUT
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
      is HomeModuleModel.CarouselMovies -> bindCarousel(model, model.carouselMediaListModel)
      HomeModuleModel.WatchlistNotLogged -> bindWatchListNotLogged()
      HomeModuleModel.WatchlistWithoutContent -> bindWatchListWithoutContent()
    }

    private fun bindWatchListWithoutContent() {
    }

    private fun bindWatchListNotLogged() {
      view.findViewById<View>(R.id.sig_in).setSafeOnClickListener { onSigInClickListener() }
    }

    private fun bindCarousel(model: HomeModuleModel.CarouselMovies, item: CarouselMediaListModel) {
      val component = view.findViewById<CarouselMediaListView>(R.id.item_view)
      component.onSeeAllClickListener = { carouselMoviesModel ->
        onCarouselMoviesModel(model, carouselMoviesModel)
      }
      component.onMovieClickListener = { carouselMoviesModel, movieItemModel ->
        onMovieClickListener(model, carouselMoviesModel, movieItemModel)
      }
      component.onAddMovieToWatchListClickListener = { carouselMovieListModel, movieListItemModel ->
        onAddMovieToWatchListClickListener(model, carouselMovieListModel, movieListItemModel)
      }
      component.onRemoveMovieFromWatchListClickListener =
        { carouselMovieListModel, movieListItemModel ->
          onRemoveMovieFromWatchListClickListener(
            model,
            carouselMovieListModel,
            movieListItemModel
          )
        }
      component.setModel(item)
    }
  }
}
