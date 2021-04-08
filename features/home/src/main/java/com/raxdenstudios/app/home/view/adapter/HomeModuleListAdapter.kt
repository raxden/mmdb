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

internal class HomeModuleListAdapter :
  BaseListAdapter<HomeModuleModel, HomeModuleListAdapter.HomeListAdapterHolder>(
    areItemsTheSame = { oldItem, newItem -> oldItem == newItem }
  ) {

  var onAddMovieToWatchListClickListener: (HomeModuleModel.CarouselMovies, CarouselMovieListModel, MovieListItemModel) -> Unit =
    { _, _, _ -> }
  var onRemoveMovieFromWatchListClickListener: (HomeModuleModel.CarouselMovies, CarouselMovieListModel, MovieListItemModel) -> Unit =
    { _, _, _ -> }
  var onMovieClickListener: (HomeModuleModel, CarouselMovieListModel, MovieListItemModel) -> Unit =
    { _, _, _ -> }

  override fun getItemViewType(position: Int) = when (getItem(position)) {
    is HomeModuleModel.CarouselMovies -> R.layout.carousel_movie_list_view_item
    else -> throw IllegalStateException("module don't supported by adapter")
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListAdapterHolder {
    val view = inflateView(parent, viewType)
    return HomeListAdapterHolder(view)
  }

  private fun inflateView(parent: ViewGroup, viewType: Int) =
    LayoutInflater.from(parent.context).inflate(viewType, parent, false)

  override fun onBindViewHolder(holder: HomeListAdapterHolder, position: Int) =
    when (val item = getItem(position)) {
      is HomeModuleModel.CarouselMovies -> holder.bind(item, item.carouselMovieListModel)
    }

  inner class HomeListAdapterHolder(
    private val view: View
  ) : RecyclerView.ViewHolder(view) {

    fun bind(modelHome: HomeModuleModel.CarouselMovies, item: CarouselMovieListModel) {
      val component = view.findViewById<CarouselMovieListView>(R.id.item_view)
      component.onMovieClickListener = { carouselMoviesModel, movieItemModel ->
        onMovieClickListener(modelHome, carouselMoviesModel, movieItemModel)
      }
      component.onAddMovieToWatchListClickListener = { carouselMovieListModel, movieListItemModel ->
        onAddMovieToWatchListClickListener(modelHome, carouselMovieListModel, movieListItemModel)
      }
      component.onRemoveMovieFromWatchListClickListener =
        { carouselMovieListModel, movieListItemModel ->
          onRemoveMovieFromWatchListClickListener(
            modelHome,
            carouselMovieListModel,
            movieListItemModel
          )
        }
      component.setModel(item)
    }
  }
}
