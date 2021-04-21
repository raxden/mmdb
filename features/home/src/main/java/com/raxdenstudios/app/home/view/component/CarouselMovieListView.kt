package com.raxdenstudios.app.home.view.component

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.raxdenstudios.app.home.R
import com.raxdenstudios.app.home.databinding.CarouselMovieListViewBinding
import com.raxdenstudios.app.home.view.adapter.CarouselMovieListAdapter
import com.raxdenstudios.app.home.view.model.CarouselMovieListModel
import com.raxdenstudios.app.movie.view.model.MediaListItemModel
import com.raxdenstudios.commons.ext.inflateView
import com.raxdenstudios.commons.ext.setSafeOnClickListener
import com.raxdenstudios.commons.ext.viewBinding

internal class CarouselMovieListView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

  private val binding: CarouselMovieListViewBinding by viewBinding()

  private val adapter: CarouselMovieListAdapter by lazy { CarouselMovieListAdapter() }

  var onAddMovieToWatchListClickListener: (CarouselMovieListModel, MediaListItemModel) -> Unit =
    { _, _ -> }
  var onRemoveMovieFromWatchListClickListener: (CarouselMovieListModel, MediaListItemModel) -> Unit =
    { _, _ -> }
  var onMovieClickListener: (CarouselMovieListModel, MediaListItemModel) -> Unit =
    { _, _ -> }
  var onSeeAllClickListener: (CarouselMovieListModel) -> Unit = {}

  init {
    if (isInEditMode) {
      inflateView(R.layout.carousel_movie_list_view, true)
    } else {
      binding.recyclerView.adapter = adapter
    }
  }

  fun setModel(model: CarouselMovieListModel) {
    binding.populate(model)
  }

  private fun CarouselMovieListViewBinding.populate(model: CarouselMovieListModel) {
    carouselTitleView.text = model.label
    adapter.submitList(model.medias)
    adapter.onMovieClickListener = { item -> onMovieClickListener(model, item) }
    adapter.onAddMovieToWatchListClickListener =
      { item -> onAddMovieToWatchListClickListener(model, item) }
    adapter.onRemoveMovieFromWatchListClickListener =
      { item -> onRemoveMovieFromWatchListClickListener(model, item) }
    carouselSeeAllArrowViewGroup.setSafeOnClickListener { onSeeAllClickListener(model) }
  }
}
