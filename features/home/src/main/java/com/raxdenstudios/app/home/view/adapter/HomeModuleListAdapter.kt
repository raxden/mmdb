package com.raxdenstudios.app.home.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raxdenstudios.app.base.BaseListAdapter
import com.raxdenstudios.app.home.R
import com.raxdenstudios.app.home.view.component.CarouselMovieListView
import com.raxdenstudios.app.home.view.model.CarouselMovieListModel
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.home.view.model.MovieListItemModel
import com.raxdenstudios.commons.ext.setSafeOnClickListener

internal class HomeModuleListAdapter :
  BaseListAdapter<HomeModuleModel, HomeModuleListAdapter.HomeListAdapterHolder>(
    areItemsTheSame = { oldItem, newItem -> oldItem == newItem }
  ) {

  companion object {
    private val CAROUSEL_MOVIES_LAYOUT = R.layout.carousel_movie_list_view_item
    private val WATCHLIST_NOT_LOGGED = R.layout.sigin_watch_list_view
    private val WATCHLIST_WITHOUT_CONTENT = R.layout.empty_watch_list_view
  }

  var onAddMovieToWatchListClickListener: (HomeModuleModel, CarouselMovieListModel, MovieListItemModel) -> Unit =
    { _, _, _ -> }
  var onRemoveMovieFromWatchListClickListener: (HomeModuleModel, CarouselMovieListModel, MovieListItemModel) -> Unit =
    { _, _, _ -> }
  var onMovieClickListener: (HomeModuleModel, CarouselMovieListModel, MovieListItemModel) -> Unit =
    { _, _, _ -> }
  var onSigInClickListener: (HomeModuleModel) -> Unit = {}

  override fun getItemViewType(position: Int): Int = when (getItem(position)) {
    is HomeModuleModel.CarouselMovies -> CAROUSEL_MOVIES_LAYOUT
    is HomeModuleModel.WatchList.WithContent -> CAROUSEL_MOVIES_LAYOUT
    is HomeModuleModel.WatchList.NotLogged -> WATCHLIST_NOT_LOGGED
    is HomeModuleModel.WatchList.WithoutContent -> WATCHLIST_WITHOUT_CONTENT
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
      is HomeModuleModel.CarouselMovies -> bindCarousel(model, model.carouselMovieListModel)
      is HomeModuleModel.WatchList.WithContent -> bindCarousel(model, model.carouselMovieListModel)
      is HomeModuleModel.WatchList.NotLogged -> bindWatchListNotLogged(model)
      is HomeModuleModel.WatchList.WithoutContent -> bindWatchListWithoutContent()
    }

    private fun bindWatchListWithoutContent() {}

    private fun bindWatchListNotLogged(model: HomeModuleModel) {
      view.findViewById<View>(R.id.sig_in).setSafeOnClickListener { onSigInClickListener(model) }
    }

    private fun bindCarousel(model: HomeModuleModel, item: CarouselMovieListModel) {
      val component = view.findViewById<CarouselMovieListView>(R.id.item_view)
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
