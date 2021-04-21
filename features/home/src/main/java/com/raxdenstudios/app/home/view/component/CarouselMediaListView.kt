package com.raxdenstudios.app.home.view.component

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.raxdenstudios.app.home.R
import com.raxdenstudios.app.home.databinding.CarouselMovieListViewBinding
import com.raxdenstudios.app.home.view.adapter.CarouselMediaListAdapter
import com.raxdenstudios.app.home.view.model.CarouselMediaListModel
import com.raxdenstudios.app.movie.view.model.MediaListItemModel
import com.raxdenstudios.commons.ext.inflateView
import com.raxdenstudios.commons.ext.setSafeOnClickListener
import com.raxdenstudios.commons.ext.viewBinding

internal class CarouselMediaListView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

  private val binding: CarouselMovieListViewBinding by viewBinding()

  private val adapter: CarouselMediaListAdapter by lazy { CarouselMediaListAdapter() }

  var onAddMovieToWatchListClickListener: (CarouselMediaListModel, MediaListItemModel) -> Unit =
    { _, _ -> }
  var onRemoveMovieFromWatchListClickListener: (CarouselMediaListModel, MediaListItemModel) -> Unit =
    { _, _ -> }
  var onMovieClickListener: (CarouselMediaListModel, MediaListItemModel) -> Unit =
    { _, _ -> }
  var onSeeAllClickListener: (CarouselMediaListModel) -> Unit = {}

  init {
    if (isInEditMode) {
      inflateView(R.layout.carousel_movie_list_view, true)
    } else {
      binding.recyclerView.adapter = adapter
    }
  }

  fun setModel(model: CarouselMediaListModel) {
    binding.populate(model)
  }

  private fun CarouselMovieListViewBinding.populate(model: CarouselMediaListModel) {
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
